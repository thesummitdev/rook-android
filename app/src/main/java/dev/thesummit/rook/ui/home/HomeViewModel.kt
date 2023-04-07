package dev.thesummit.rook.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dev.thesummit.rook.R
import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinksFeed
import dev.thesummit.rook.utils.ErrorMessage
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface HomeUiState {
  val isLoading: Boolean
  val errorMessages: List<ErrorMessage>
  val searchInput: String

  /**
   * There are no links to render
   *
   * @property isLoading
   * @property errorMessages
   * @property searchInput
   */
  data class NoLinks(
      override val isLoading: Boolean,
      override val errorMessages: List<ErrorMessage>,
      override val searchInput: String,
  ) : HomeUiState

  /**
   * There are links to render as contained in [links].
   *
   * @property links
   * @property selectedLink
   * @property isLoading
   * @property errorMessages
   * @property searchInput
   */
  data class HasLinks(
      val linksFeed: LinksFeed,
      // val selectedLink: Link,
      override val isLoading: Boolean,
      override val errorMessages: List<ErrorMessage>,
      override val searchInput: String,
  ) : HomeUiState
}

/**
 * Internal representation of Home route state, in a raw form.
 *
 * @property links
 * @property isLoading
 * @property errorMessages
 * @property searchInput
 */
private data class HomeViewModelState(
    val linksFeed: LinksFeed? = null,
    // val selectedLinkId: Int? = null,
    val isLoading: Boolean = false,
    val errorMessages: List<ErrorMessage> = emptyList(),
    val searchInput: String = "",
) {

  /**
   * Converts this [HomeViewModelState] into a more strongly typed [HomeUiState] for driving the ui.
   *
   * @return
   */
  fun toUiState(): HomeUiState =
      if (linksFeed == null) {
        HomeUiState.NoLinks(
            isLoading = isLoading,
            errorMessages = errorMessages,
            searchInput = searchInput
        )
      } else {
        HomeUiState.HasLinks(
            linksFeed = linksFeed,
            isLoading = isLoading,
            errorMessages = errorMessages,
            searchInput = searchInput
        )
      }
}

class HomeViewModel(private val linksRepository: LinksRepository) : ViewModel() {
  private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))

  // UI state exposed to the UI
  val uiState =
      viewModelState
          .map(HomeViewModelState::toUiState)
          .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

  init {
    refreshLinks()
  }

  fun refreshLinks() {
    viewModelState.update { it.copy(isLoading = true) }

    viewModelScope.launch {
      val result = linksRepository.getLinks()
      viewModelState.update {
        when (result) {
          is Result.Success -> it.copy(linksFeed = LinksFeed(result.data), isLoading = false)
          is Result.Error -> {
            val errorMessages =
                it.errorMessages +
                    ErrorMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        messageId = R.string.load_error
                    )
            it.copy(errorMessages = errorMessages, isLoading = false)
          }
        }
      }
    }
  }

  companion object {
    fun provideFactory(
        linksRepository: LinksRepository,
    ): ViewModelProvider.Factory =
        // Handled by compose compiler
        @Suppress("JVM_DEFAULT_THROUGH_INHERITANCE")
        object : ViewModelProvider.Factory {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(linksRepository) as T
          }
        }
  }
}