/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.example.appsearchsample.model

import android.app.Application
import androidx.appsearch.app.SearchResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.example.appsearchsample.NoteAppSearchManager
import java.util.UUID
import kotlinx.coroutines.launch

/**
 * ViewModel for interacting with [Note] documents.
 */
class NoteViewModel(application: Application) : AndroidViewModel(application) {

  // Posts an error message if an AppSearch operation fails.
  private val _errorMessageLiveData = MutableLiveData<String?>()
  val errorMessageLiveData: LiveData<String?> = _errorMessageLiveData

  private val _noteLiveData: MutableLiveData<List<SearchResult>> = MutableLiveData(mutableListOf())
  private val noteLiveData: LiveData<List<SearchResult>> = _noteLiveData

  private val noteAppSearchManager: NoteAppSearchManager =
    NoteAppSearchManager(getApplication(), viewModelScope)

  /**
   * Adds a new [Note] document to the AppSearch database.
   *
   * After the [Note] document is added, the database is queried to update
   * the note list with all notes in the database (including the added [Note]
   * document).
   *
   * On error, posts a message to [errorMessageLiveData].
   *
   * @param text text to create [Note] document for.
   */
  fun addNote(text: String) {
    val id = UUID.randomUUID().toString()
    val note = Note(id = id, text = text)
    viewModelScope.launch {
      val result = noteAppSearchManager.addNote(note)
      if (!result.isSuccess) {
        _errorMessageLiveData.postValue("Failed to add note with id: $id and text: $text")
      }

      queryNotes()
    }
  }

  /**
   * Removes a [Note] document from the AppSearch database.
   *
   * After the [Note] document is removed, the database is queried to update the the note list with
   * all notes in the database.
   *
   * On error, posts a message to [errorMessageLiveData].
   */
  fun removeNote(namespace: String, id: String) {
    viewModelScope.launch {
      val result = noteAppSearchManager.removeNote(namespace, id)
      if (!result.isSuccess) {
        _errorMessageLiveData.postValue(
          "Failed to remove note in namespace: $namespace with id: $id"
        )
      }

      queryNotes()
    }
  }

  /**
   * Retrieves [SearchResult] objects that match the query from the AppSearch
   * database.
   *
   * If no query is provided, this retrieves all [SearchResult] objects in the
   * database.
   */
  fun queryNotes(query: String = ""): LiveData<List<SearchResult>> {
    viewModelScope.launch {
      val resultNotes = noteAppSearchManager.queryLatestNotes(query)
      _noteLiveData.postValue(resultNotes)
    }
    return noteLiveData
  }

  /**
   * Factory for creating a [NoteAppSearchManager] instance.
   */
  class NoteViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
        @Suppress("UNCHECKED_CAST")
        return NoteViewModel(application) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
    }
  }
}
