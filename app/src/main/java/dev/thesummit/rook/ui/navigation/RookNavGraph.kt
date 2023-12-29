package dev.thesummit.rook.ui.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.thesummit.rook.ui.create.CreateRoute
import dev.thesummit.rook.ui.home.HomeRoute
import dev.thesummit.rook.ui.settings.SettingsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RookNavGraph(
    isExpandedScreen: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    openDrawer: () -> Unit = {},
    startDestination: String = RookDestinations.HOME_ROUTE,
) {

  NavHost(
      navController = navController,
      startDestination = startDestination,
      modifier = modifier,
  ) {
    composable(RookDestinations.HOME_ROUTE) {
      HomeRoute(
          isExpandedScreen = isExpandedScreen,
          openDrawer = openDrawer,
          navController = navController,
      )
    }

    composable(RookDestinations.SETTINGS_ROUTE) {
      SettingsRoute(isExpandedScreen = isExpandedScreen, openDrawer = openDrawer)
    }

    composable(RookDestinations.CREATE_ROUTE) { CreateRoute(openDrawer = openDrawer) }
  }
}
