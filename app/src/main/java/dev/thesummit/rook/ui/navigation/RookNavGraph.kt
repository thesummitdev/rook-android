package dev.thesummit.rook.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.thesummit.rook.ui.create.CreateRoute
import dev.thesummit.rook.ui.home.HomeRoute
import dev.thesummit.rook.ui.settings.SettingsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RookNavGraph(
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = RookDestinations.HOME_ROUTE,
) {

  val navController = LocalNavController.current

  NavHost(
      navController = navController,
      startDestination = startDestination,
      modifier = modifier,
  ) {
    composable(RookDestinations.HOME_ROUTE) { HomeRoute() }

    composable(RookDestinations.SETTINGS_ROUTE) { SettingsRoute() }

    composable(RookDestinations.CREATE_ROUTE) { CreateRoute() }
  }
}
