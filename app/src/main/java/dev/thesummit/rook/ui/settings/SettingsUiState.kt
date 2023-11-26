package dev.thesummit.rook.ui.settings

import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.utils.ErrorMessage

sealed interface SettingsUiState {
  val isLoading: Boolean
  val errorMessages: List<ErrorMessage>

  data class isInitialized(
      override val isLoading: Boolean,
      override val errorMessages: List<ErrorMessage>,
      val host: Setting,
      val apiKey: Setting,
  ) : SettingsUiState

  data class NotInitialized(
      override val isLoading: Boolean,
      override val errorMessages: List<ErrorMessage>,
  ) : SettingsUiState
}
