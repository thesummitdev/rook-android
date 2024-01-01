package dev.thesummit.rook.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.core.content.getSystemService
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.thesummit.rook.R
import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.data.settings.SettingsRepository
import dev.thesummit.rook.http.RookApiUrlRequestCallback
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinksFeed
import dev.thesummit.rook.model.SettingKey
import dev.thesummit.rook.utils.ErrorMessage
import dev.thesummit.rook.utils.NetworkStatus
import dev.thesummit.rook.utils.NetworkStatusTracker
import dev.thesummit.rook.workers.SyncWorker
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import org.chromium.net.CronetEngine
import org.chromium.net.UrlRequest

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
    val networkConnected: Boolean = true,
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

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    @ApplicationContext private val applicationContext: Context,
    private val linksRepository: LinksRepository,
    private val settingsRespository: SettingsRepository,
    private val networkStatusTracker: NetworkStatusTracker,
    private val cronetEngine: CronetEngine,
) : ViewModel() {
  private val viewModelState = MutableStateFlow(HomeViewModelState(isLoading = true))
  private val workManager = WorkManager.getInstance(applicationContext)
  private val networkStatus = networkStatusTracker.networkStatus

  // UI state exposed to the UI
  val uiState =
      viewModelState
          .map(HomeViewModelState::toUiState)
          .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.toUiState())

  init {

    viewModelScope.launch(Dispatchers.IO) {
      // Observe the device network state.
      networkStatus.collect { status ->
        when (status) {
          is NetworkStatus.Available -> {
            viewModelState.update { it.copy(networkConnected = true) }
          }
          is NetworkStatus.Unavailable -> {
            viewModelState.update { it.copy(networkConnected = false) }
          }
        }
      }
    }

    refreshLinks()
    viewModelScope.launch(Dispatchers.IO) {

      // Begin collecting links from the database.
      linksRepository.getLinks().collect { result ->
        viewModelState.update {
          when (result) {
            is Result.Success -> it.copy(linksFeed = LinksFeed(result.data), isLoading = false)
            is Result.Error -> {
              Log.i("Rook", "found result: error")
              val errorMessages =
                  it.errorMessages +
                      ErrorMessage(
                          id = UUID.randomUUID().mostSignificantBits,
                          messageId = R.string.load_error
                      )
              it.copy(linksFeed = null, errorMessages = errorMessages, isLoading = false)
            }
          }
        }
      }
    }
  }

  fun copyToClipboard(link: Link) {
    val cm: ClipboardManager =
        applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText(null, link.url)
    cm.setPrimaryClip(clip)
  }

  suspend fun deleteLink(link: Link) {

    if (!viewModelState.value.networkConnected) {
      return
    }

    val hostResult = this.settingsRespository.getSettingByKeyOnce(SettingKey.HOST.key)
    val host =
        when (hostResult) {
          is Result.Success -> hostResult.data.value
          else -> throw Exception("Could not fetch host from database")
        }
    val apiKeyResult = this.settingsRespository.getSettingByKeyOnce(SettingKey.API_KEY.key)
    val apiKey =
        when (apiKeyResult) {
          is Result.Success -> apiKeyResult.data.value
          else -> throw Exception("Could not fetch apiKey from database")
        }

    val executor: Executor = Executors.newSingleThreadExecutor()
    val requestBuilder =
        cronetEngine
            .newUrlRequestBuilder(
                """${host}/links/${link.id}""",
                RookApiUrlRequestCallback(::onDataReady),
                executor
            )
            .addHeader("Authorization", """Bearer ${apiKey}""")
            .addHeader("Content-Type", "application/json")
            .setHttpMethod("DELETE")
    val request: UrlRequest = requestBuilder.build()
    request.start()

    // remove from local database
    linksRepository.deleteLink(link)
  }

  suspend fun onDataReady(@Suppress("UNUSED_PARAMETER") response: JsonElement) {}

  fun refreshLinks() {
    if (!viewModelState.value.networkConnected) {
      return
    }

    workManager.enqueue(OneTimeWorkRequest.from(SyncWorker::class.java))
    viewModelScope.launch {
      delay(1000L) // TODO: subscribe to workmanager and hide this when the work request is done.
      viewModelState.update { it.copy(isLoading = false) }
    }
  }
}
