package dev.thesummit.rook.ui

import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed interface UiEvent {
  object ToggleSearch : UiEvent
}

@ActivityRetainedScoped
class Events @Inject constructor() {

  private val _uiEvents = MutableSharedFlow<UiEvent>()
  val uiEvents = _uiEvents.asSharedFlow()

  suspend fun sendEvent(event: UiEvent) {
    _uiEvents.emit(event)
  }
}
