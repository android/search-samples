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

import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appsearch.app.SearchResult
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.appsearchsample.model.Note

/**
 * Adapter for displaying a list of [Note] objects.
 */
class NoteListItemAdapter(private val onDelete: (SearchResult?) -> Unit) :
  ListAdapter<SearchResult, NoteListItemAdapter.NoteViewHolder>(NOTES_COMPARATOR) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): NoteViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_note, parent, /*attachToRoot=*/false)
    return NoteViewHolder(view)
  }

  override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
    holder.bind(getItem(position), onDelete)
  }

  /** ViewHolder for [NoteListItemAdapter]. */
  inner class NoteViewHolder(
    view: View,
  ) : RecyclerView.ViewHolder(view) {
    val noteTextView: TextView = view.findViewById(R.id.note_text)
    val noteDeleteButtonView: Button = view.findViewById(R.id.note_delete_button)

    fun bind(searchResult: SearchResult, onDelete: (SearchResult?) -> Unit) {
      val note = searchResult.genericDocument.toDocumentClass(Note::class.java)
      val stringBuilder = SpannableStringBuilder(note.text)

      searchResult.matchInfos.forEach {
        if (it.propertyPath == TEXT_PROPERTY_PATH)
          stringBuilder.setSpan(StyleSpan(BOLD),
                                it.exactMatchRange.start,
                                it.exactMatchRange.end,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
      }

      noteTextView.text = stringBuilder

      noteDeleteButtonView.setOnClickListener { onDelete(searchResult) }
    }
  }

  companion object {
    private val TEXT_PROPERTY_PATH = "text"

    private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<SearchResult>() {
      override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        val oi = oldItem.genericDocument.toDocumentClass(Note::class.java)
        val ni = newItem.genericDocument.toDocumentClass(Note::class.java)
        return oi.id === ni.id &&
          oi.namespace === ni.namespace
      }

      override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.genericDocument == newItem.genericDocument
      }
    }
  }
}
