package dev.thesummit.rook.ui.create

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.thesummit.rook.data.Result
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.data.settings.SettingsRepository
import dev.thesummit.rook.http.RookApiUrlRequestCallback
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.SettingKey
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import kotlin.collections.dropWhile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest

data class CreateUiState(
    val requestPending: Boolean = false,
)

@HiltViewModel
class CreateViewModel
@Inject
constructor(
    private val linksRepository: LinksRepository,
    private val settingsRepository: SettingsRepository,
    private val cronetEngine: CronetEngine
) : ViewModel() {

  // Internal state
  private val _uiState = MutableStateFlow(CreateUiState())

  val uiState = _uiState.stateIn(viewModelScope, SharingStarted.Eagerly, _uiState.value)
  var formState by mutableStateOf(LinkForm.LinkFormState())

  /**
   * Attempts to push a new link to the Rook server, and if successful, save the response back in
   * the local device database.
   */
  fun createLink() {

    viewModelScope.launch(Dispatchers.IO) {
      _uiState.update { it.copy(requestPending = true) }
      val hostResult = settingsRepository.getSettingByKeyOnce(SettingKey.HOST.key)
      val host =
          when (hostResult) {
            is Result.Success -> hostResult.data.value
            else -> throw Exception("Could not fetch host from database")
          }
      val apiKeyResult = settingsRepository.getSettingByKeyOnce(SettingKey.API_KEY.key)
      val apiKey =
          when (apiKeyResult) {
            is Result.Success -> apiKeyResult.data.value
            else -> throw Exception("Could not fetch apiKey from database")
          }

      val data =
          """
            {
              "title": "${formState.title}",
              "url":  "${formState.url}",
              "tags": "${formState.tags}"
            }
          """

      val executor: Executor = Executors.newSingleThreadExecutor()
      val uploadDataProvider = UploadDataProviders.create(data.toByteArray())
      val requestBuilder =
          cronetEngine
              .newUrlRequestBuilder(
                  """${host}/links""",
                  RookApiUrlRequestCallback(::onDataReady),
                  executor
              )
              .addHeader("Authorization", """Bearer ${apiKey}""")
              .addHeader("Content-Type", "application/json")
              .setHttpMethod("PUT")
              .setUploadDataProvider(uploadDataProvider, executor)
      val request: UrlRequest = requestBuilder.build()
      request.start()
    }
  }

  fun onEvent(event: CreateUiEvent) {
    when (event) {
      is CreateUiEvent.TitleChanged -> {
        formState = formState.copy(title = event.title)
        if (event.title.isEmpty()) {
          formState =
              formState.copy(
                  titleErrorMessage = "A title is required",
                  invalidFields =
                      setOf(*formState.invalidFields.toTypedArray(), LinkForm.Fields.TITLE)
              )
        } else {
          formState =
              formState.copy(
                  titleErrorMessage = "",
                  invalidFields =
                      formState.invalidFields.dropWhile { it == LinkForm.Fields.TITLE }.toSet()
              )
        }
      }
      is CreateUiEvent.UrlChanged -> {
        formState = formState.copy(url = event.url)
        if (event.url.isEmpty()) {
          formState =
              formState.copy(
                  urlErrorMessage = "Url is required",
                  invalidFields =
                      setOf(*formState.invalidFields.toTypedArray(), LinkForm.Fields.URL)
              )
        } else if (Patterns.WEB_URL.matcher(event.url).matches()) {
          formState =
              formState.copy(
                  urlErrorMessage = "",
                  invalidFields =
                      formState.invalidFields.dropWhile { it == LinkForm.Fields.URL }.toSet()
              )
        } else {
          formState =
              formState.copy(
                  urlErrorMessage = "Malformed Url",
                  invalidFields =
                      setOf(*formState.invalidFields.toTypedArray(), LinkForm.Fields.URL)
              )
        }
      }
      is CreateUiEvent.TagsChanged -> {
        formState = formState.copy(tags = event.tags)
      }
      is CreateUiEvent.Submit -> {
        createLink()
      }
    }
  }

  /**
   * Receives the response JSON from the Rook server and decodes it, and adds the newly created link
   * to the local database.
   */
  suspend fun onDataReady(response: JsonElement) {
    val link: Link = Json.decodeFromJsonElement<Link>(response)
    linksRepository.addLink(link)

    formState = LinkForm.LinkFormState()
    _uiState.update { it.copy(requestPending = false) }
  }
}
