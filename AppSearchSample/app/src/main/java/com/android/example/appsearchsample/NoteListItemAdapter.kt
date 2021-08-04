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

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.appsearchsample.databinding.ItemNoteBinding
import com.android.example.appsearchsample.model.Note

/**
 * Adapter for displaying a list of {@link Note} objects.
 */
class NoteListItemAdapter(private val onDelete: (Note?) -> Unit) :
  ListAdapter<Note, NoteListItemAdapter.NoteViewHolder>(NOTES_COMPARATOR) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): NoteViewHolder {
    val view: ItemNoteBinding = ItemNoteBinding.inflate(
      LayoutInflater.from(parent.context),
      parent, /*attachToRoot=*/
      false
    )

    return NoteViewHolder(view)
  }

  override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    holder.bind(getItem(position), onDelete)
  }

  /** ViewHolder for {@link NoteListItemAdapter}. */
  class NoteViewHolder(
    binding: ItemNoteBinding,
  ) : RecyclerView.ViewHolder(binding.root) {
    private val noteTextView: TextView = binding.noteText
    private val noteDeleteButtonView: Button = binding.noteDeleteButton

    fun bind(note: Note?, onDelete: (Note?) -> Unit) {
      noteTextView.text = note?.text
      noteDeleteButtonView.setOnClickListener { onDelete(note) }
    }
  }

  companion object {
    private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<Note>() {
      override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        // TODO: Check for the same Note id instead of text.
        return oldItem.text === newItem.text
      }

      override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.text == newItem.text
      }
    }
  }
}