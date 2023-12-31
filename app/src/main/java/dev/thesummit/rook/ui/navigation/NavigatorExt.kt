package dev.thesummit.rook.ui.navigation

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun Navigator.setupWithNavController(
    activity: ComponentActivity,
    navController: NavHostController
) {

  activity.lifecycleScope.launch {
    activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
      navEvents.collect { direction ->
        when (direction) {
          is Navigator.Direction.NavigateTo -> {
            navController.navigate(direction.route, direction.navOptions)
          }
          is Navigator.Direction.NavigateBack -> {
            if (!navController.popBackStack()) {

              activity.finish()
            }
          }
        }
      }
    }
  }
}
