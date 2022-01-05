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

import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchSchema
import androidx.appsearch.app.GenericDocument

/**
 * Data representation for a user-created [Note] object.
 *
 * The [Document] annotation marks this class as an AppSearch document class.
 * This allows the AppSearch compiler to generate classes required to convert
 * this class to and from [GenericDocument], the basic unit of data in
 * AppSearch.
 */
@Document
data class Note(
  /** Namespace for Note */
  @Document.Namespace
  val namespace: String = "user",

  /** Id for Note */
  @Document.Id
  val id: String,

  /** Field for text that that user inputs */
  @Document.StringProperty(
    indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES
  )
  val text: String
)
