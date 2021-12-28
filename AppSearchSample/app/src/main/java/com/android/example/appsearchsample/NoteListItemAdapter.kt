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

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.appsearchsample.databinding.ItemNoteBinding
import com.android.example.appsearchsample.model.Note

/**
 * Adapter for displaying a list of [Note] objects.
 */
class NoteListItemAdapter(private val onDelete: (Note?) -> Unit) :
  ListAdapter<Note, NoteListItemAdapter.NoteViewHolder>(NOTES_COMPARATOR) {

  var query: String = ""

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): NoteViewHolder {
    val view: ItemNoteBinding = ItemNoteBinding.inflate(
      LayoutInflater.from(parent.context),
      parent,
      /*attachToRoot=*/false
    )

    return NoteViewHolder(view)
  }

  override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    holder.bind(getItem(position), onDelete, position)
  }

  /** ViewHolder for [NoteListItemAdapter]. */
  inner class NoteViewHolder(
    binding: ItemNoteBinding,
  ) : RecyclerView.ViewHolder(binding.root) {
    private val noteTextView = binding.noteText
    private val noteDeleteButtonView = binding.noteDeleteButton

    fun bind(note: Note?, onDelete: (Note?) -> Unit, position: Int) {
      val sb = SpannableStringBuilder()
      val queryList: List<String> = query.split(' ')
      note?.text?.split(' ')?.forEach {
        if (queryList.contains(it))
          sb.bold { append(it) }
        else
          sb.append(it)
        sb.append(" ")
      }

      sb.removeRange(sb.length-1, sb.length)

      noteTextView.text = sb

      if (position == 0)
        noteTextView.setBackgroundColor(Color.RED)

      noteDeleteButtonView.setOnClickListener { onDelete(note) }
    }
  }

  companion object {
    private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<Note>() {
      override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id === newItem.id &&
          oldItem.namespace === newItem.namespace
      }

      override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
      }
    }
  }
}
