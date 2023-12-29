package dev.thesummit.rook.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavController

object RookDestinations {
  const val HOME_ROUTE = "home"
  const val SETTINGS_ROUTE = "settings"
  const val CREATE_ROUTE = "create"
}

fun NavController.navigateToCreate(navOptions:NavOptions? = null) {
  this.navigate(RookDestinations.CREATE_ROUTE, navOptions)
}

class RookNavigationActions(navController: NavHostController) {

  val navigateToHome: () -> Unit = {
    navController.navigate(RookDestinations.HOME_ROUTE) {
      // Pop up to the start destination of the graph to
      // avoid building up a large stack of destinations
      // on the back stack as users select items
      popUpTo(navController.graph.findStartDestination().id) { saveState = true }

      // Avoid multiple copies of the same destination when
      // reselecting the same item
      launchSingleTop = true

      // Restore state when reselecting a previously selected item
      restoreState = true
    }
  }

  val navigateToSettings: () -> Unit = {
    navController.navigate(RookDestinations.SETTINGS_ROUTE) {
      // Pop up to the start destination of the graph to
      // avoid building up a large stack of destinations
      // on the back stack as users select items
      popUpTo(navController.graph.findStartDestination().id) { saveState = true }

      // Avoid multiple copies of the same destination when
      // reselecting the same item
      launchSingleTop = true

      // Restore state when reselecting a previously selected item
      restoreState = true
    }
  }
}
