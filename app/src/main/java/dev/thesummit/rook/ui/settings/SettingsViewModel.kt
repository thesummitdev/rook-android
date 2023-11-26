package dev.thesummit.rook.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.settings.SettingsRepository
import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.model.SettingKey
import dev.thesummit.rook.utils.ErrorMessage
import dev.thesummit.rook.workers.LoginWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Representation of the internal [SettingsViewModel] state.
 *
 * @property isLoading
 * @property errorMessages
 * @property host
 * @property apiKey
 */
private data class SettingsViewModelState(
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val host: Setting? = null,
    val apiKey: Setting? = null,
) {

  fun toUiState(): SettingsUiState =
      if (host is Setting && apiKey is Setting)
          SettingsUiState.isInitialized(
              isLoading = isLoading,
              errorMessages = errorMessages,
              host = host,
              apiKey = apiKey,
          )
      else
          SettingsUiState.NotInitialized(
              isLoading = isLoading,
              errorMessages = errorMessages,
          )
}

class SettingsViewModel(
    val applicationContext: Context,
    val settingsRepository: SettingsRepository
) : ViewModel() {

  companion object {
    fun provideFactory(
        applicationContext: Context,
        settingsRepository: SettingsRepository
    ): ViewModelProvider.Factory =
        // Handled by compose compiler
        @Suppress("JVM_DEFAULT_THROUGH_INHERITANCE")
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(applicationContext, settingsRepository) as T
          }
        }
  }

  private val workManager = WorkManager.getInstance(applicationContext)
  // Keep internal mutable state.
  private val viewModelState = MutableStateFlow(SettingsViewModelState(isLoading = true))

  // Expose the neccessary UI state to the UI
  val uiState =
      viewModelState
          .map(SettingsViewModelState::toUiState)
          .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

  init {
    collectSettingKeys()
  }

  private fun collectSettingKeys() {
    viewModelScope.launch {
      combine(
              // Listen to these keys from the database and update the UI state.
              settingsRepository.getSettingByKey(SettingKey.HOST.key),
              settingsRepository.getSettingByKey(SettingKey.API_KEY.key),
          ) { hostResult, apiKeyResult ->
        val host: Setting? =
            when (hostResult) {
              is Result.Success -> hostResult.data
              is Result.Error -> null
            }

        val apiKey: Setting? =
            when (apiKeyResult) {
              is Result.Success -> apiKeyResult.data
              is Result.Error -> null
            }

        arrayOf(host, apiKey)
      }
          .collect { result ->
            val (host, apiKey) = result
            viewModelState.update { it.copy(host = host, apiKey = apiKey) }
          }
    }
  }

  fun attemptToSignIn(serverAddress: String, username: String, password: String): Unit {

    workManager.enqueue(
        OneTimeWorkRequestBuilder<LoginWorker>()
            .setInputData(
                workDataOf(
                    "server_address" to serverAddress,
                    "username" to username,
                    "password" to password,
                )
            )
            .build()
    )
  }

  /**
   * Logs the user out and destroys any login specific Settings Note: this does not remove all user
   * data.
   */
  fun logout(): Unit {
    viewModelScope.launch {
      settingsRepository.dropSettings(listOf(SettingKey.HOST.key, SettingKey.API_KEY.key))
    }
  }
}
