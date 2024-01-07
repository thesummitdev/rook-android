package dev.thesummit.rook.ui.create

import androidx.compose.ui.text.input.TextFieldValue

sealed class CreateUiEvent {
  data class TitleChanged(val title: String) : CreateUiEvent()
  data class UrlChanged(val url: String) : CreateUiEvent()
  data class TagsChanged(val value: TextFieldValue) : CreateUiEvent()
  object Submit : CreateUiEvent()
}

