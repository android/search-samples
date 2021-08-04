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

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.example.appsearchsample.databinding.ActivityMainBinding
import com.android.example.appsearchsample.model.NoteViewModel
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

/**
 * Activity to set up a simple AppSearch demo.
 *
 * <p>This activity allows the user add a note by inputting their desired text
 * into the dialog prompt. Once a note is added, it is indexed into AppSearch.
 * The user can then use the search bar to put input terms to find notes that
 * match.
 *
 * <p>By default, the notes list displays all notes that have been indexed. Once
 * the user submits a query, the list is updated to reflect notes that match
 * the query.
 */
class MainActivity : AppCompatActivity() {
  private val noteViewModel: NoteViewModel by viewModels()

  private lateinit var activityBinding: ActivityMainBinding
  private lateinit var searchView: SearchView
  private lateinit var notesAdapter: NoteListItemAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activityBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(activityBinding.root)

    initAddNoteButtonListener()
    initNoteListView()

    noteViewModel.noteLiveData.observe(
      this,
      {
        notesAdapter.submitList(it)
        activityBinding.progressSpinner.visibility = View.GONE
        if (it.isEmpty()) {
          activityBinding.notesList.visibility = View.GONE
          activityBinding.noNotesMessage.visibility = View.VISIBLE
        } else {
          activityBinding.notesList.visibility = View.VISIBLE
          activityBinding.noNotesMessage.visibility = View.GONE
        }
      })
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.options_menu, menu)
    searchView = menu.findItem(R.id.search_bar).actionView as SearchView

    initQueryListener()

    return true
  }

  /** Initializes listeners for query input. */
  private fun initQueryListener() {
    searchView.queryHint = getString(R.string.search_bar_hint)
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        // TODO: Submit query to AppSearch, and update adapter with results.
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        return false
      }
    })
  }

  /**
   * Initializes listener for insert note button.
   *
   * <p>The listener configures an alert dialog for the user to input text
   * to save as a {@link Note} object.
   */
  private fun initAddNoteButtonListener() {
    val insertNoteButton: ExtendedFloatingActionButton =
      activityBinding.insertNoteButton

    insertNoteButton.setOnClickListener {
      val dialogBuilder = AlertDialog.Builder(this@MainActivity)
      dialogBuilder.setView(R.layout.add_note_dialog)
        .setCancelable(false)
        .setPositiveButton(R.string.add_note_dialog_save) { dialog, _ ->
          val addNoteDialogView = dialog as AlertDialog
          val noteEditText =
            addNoteDialogView.findViewById(R.id.add_note_text) as EditText?
          val noteText = noteEditText?.text.toString()
          activityBinding.progressSpinner.visibility = View.VISIBLE
          activityBinding.noNotesMessage.visibility = View.GONE
          activityBinding.notesList.visibility = View.GONE
          noteViewModel.addNote(noteText)
        }
        .setNegativeButton(R.string.add_note_dialog_cancel) { dialog, _ ->
          dialog.cancel()
        }
      val alert = dialogBuilder.create()
      alert.setTitle(R.string.add_note_dialog_title)
      alert.show()
    }
  }

  /** Initializes recycler view for list of {@link Note} objects. */
  private fun initNoteListView() {
    notesAdapter = NoteListItemAdapter {
      // TODO: Call Note ViewModel to delete Note
    }
    activityBinding.notesList.adapter = notesAdapter
    activityBinding.notesList.addItemDecoration(
      DividerItemDecoration(
        this,
        LinearLayoutManager.VERTICAL
      )
    )
    activityBinding.notesList.layoutManager = LinearLayoutManager(this)
  }
}
