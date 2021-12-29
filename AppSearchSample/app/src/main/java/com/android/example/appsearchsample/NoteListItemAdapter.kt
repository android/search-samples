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
import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appsearch.app.SearchResult
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.example.appsearchsample.databinding.ItemNoteBinding
import com.android.example.appsearchsample.model.Note

/**
 * Adapter for displaying a list of [Note] objects.
 */
class NoteListItemAdapter(private val onDelete: (SearchResult?) -> Unit) :
  ListAdapter<SearchResult, NoteListItemAdapter.NoteViewHolder>(NOTES_COMPARATOR) {

  var query: String = ""
  var snippets: List<List<SearchResult.MatchInfo>>? = null

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

    fun bind(sr: SearchResult, onDelete: (SearchResult?) -> Unit, position: Int) {
      val note = sr.genericDocument.toDocumentClass(Note::class.java)
      val sb = SpannableStringBuilder(note.text)

      sr.matchInfos.forEach {
        sb.setSpan(StyleSpan(BOLD), it.exactMatchRange.start, it.exactMatchRange.end, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
      }

      noteTextView.text = sb

      noteTextView.setBackgroundColor(Color.parseColor(note.color))

      noteDeleteButtonView.setOnClickListener { onDelete(sr) }
    }
  }

  companion object {
    private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<SearchResult>() {
      override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        val oi = oldItem.genericDocument.toDocumentClass(Note::class.java)
        val ni = newItem.genericDocument.toDocumentClass(Note::class.java)
        return oi.id === ni.id &&
          oi.namespace === ni.namespace
      }

      override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
        return oldItem.genericDocument.toDocumentClass(Note::class.java) ==
          newItem.genericDocument.toDocumentClass(Note::class.java)
      }
    }
  }
}
