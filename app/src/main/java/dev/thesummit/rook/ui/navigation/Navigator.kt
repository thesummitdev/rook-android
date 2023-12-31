package dev.thesummit.rook.ui.navigation

import javax.inject.Inject
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import androidx.navigation.NavOptions

@ActivityRetainedScoped
class Navigator @Inject constructor() {

  private val _navEvents = Channel<Direction>(Channel.UNLIMITED)
  val navEvents: Flow<Direction> = _navEvents.receiveAsFlow()

  fun navigateTo(route:String) {
    _navEvents.trySend(Direction.NavigateTo(route))
  }

  fun navigateBack(){
    _navEvents.trySend(Direction.NavigateBack)
  }

  sealed interface Direction {
    data class NavigateTo(
      val route: String,
      val navOptions: NavOptions? = null,
    ): Direction

    object NavigateBack : Direction
  }

}
