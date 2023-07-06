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
package com.android.example.appsearchsample

import android.content.Context
import androidx.appsearch.app.SearchResult
import com.android.example.appsearchsample.model.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Manages interactions with AppSearch.
 *
 * [NoteAppSearchManager] operations can be called once the instance is initialized. This is
 * indicated by the [isInitialized] property emitting value ```true```.
 */
class NoteAppSearchManager(context: Context, coroutineScope: CoroutineScope) {
  private val isInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)

  init {
    coroutineScope.launch {

      try {
        // Set the [NoteAppSearchManager] instance as initialized to allow AppSearch operations to
        // be called.
        isInitialized.value = true

        awaitCancellation()
      } finally {
      }
    }
  }

  /**
   * Adds a [Note] document to the AppSearch database.
   */
  suspend fun addNote(note: Note) {
    awaitInitialization()
  }

  /**
   * Queries the AppSearch database for matching [Note] documents.
   *
   * @return a list of [SearchResult] objects. This returns SearchResults in the order
   * they were created (with most recent first). This returns a maximum of 10
   * SearchResults that match the query, per AppSearch default page size.
   * Snippets are returned for the first 10 results.
   */
  suspend fun queryLatestNotes(query: String): List<SearchResult> {
    awaitInitialization()
    return emptyList()
  }

  /**
   * Removes [Note] document from the AppSearch database by namespace and
   * id.
   */
  suspend fun removeNote(
    namespace: String,
    id: String
  ) {
    awaitInitialization()
    return
  }

  /**
   * Awaits [isInitialized] being set to ```true```.
   */
  private suspend fun awaitInitialization() {
    if (!isInitialized.value) {
      isInitialized.first { it }
    }
  }

  companion object {
    private const val DATABASE_NAME = "notesDatabase"
  }
}
