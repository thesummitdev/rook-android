package dev.thesummit.rook.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object RookDestinations {
  const val INITIAL_ROUTE = "init"
  const val HOME_ROUTE = "home"
  const val SETTINGS_ROUTE = "settings"
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

  val navigateToInitial: () -> Unit = {
    navController.navigate(RookDestinations.INITIAL_ROUTE) {
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
