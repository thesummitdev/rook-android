package dev.thesummit.rook.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.annotation.NonNull

object RookDestinations {
  const val HOME_ROUTE = "home"
  const val SETTINGS_ROUTE = "settings"
  const val CREATE_ROUTE = "create"
}

fun NavController.navigateToCreate(navOptions:NavOptions? = null) {
  this.navigate(RookDestinations.CREATE_ROUTE, navOptions)
}

fun NavController.navigateToHome(navOptions:NavOptions? = null) {
  this.navigate(RookDestinations.HOME_ROUTE)
}

fun NavController.navigateToSettings(navOptions:NavOptions? = null) {
  this.navigate(RookDestinations.SETTINGS_ROUTE, navOptions)
}
