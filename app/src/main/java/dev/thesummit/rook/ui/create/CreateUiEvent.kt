package dev.thesummit.rook.ui.create

sealed class CreateUiEvent {
  data class TitleChanged(val title: String) : CreateUiEvent()
  data class UrlChanged(val url: String) : CreateUiEvent()
  data class TagsChanged(val tags: String) : CreateUiEvent()
  object Submit : CreateUiEvent()
}

