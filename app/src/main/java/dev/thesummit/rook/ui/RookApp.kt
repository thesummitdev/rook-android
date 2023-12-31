package dev.thesummit.rook.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.thesummit.rook.ui.components.AppNavRail
import dev.thesummit.rook.ui.navigation.RookDestinations
import dev.thesummit.rook.ui.navigation.RookNavGraph
import dev.thesummit.rook.ui.navigation.LocalNavController
import dev.thesummit.rook.ui.navigation.navigateToHome
import dev.thesummit.rook.ui.navigation.navigateToSettings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RookApp(widthSizeClass: WindowWidthSizeClass) {
  val navController = LocalNavController.current
  val coroutineScope = rememberCoroutineScope()

  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route ?: RookDestinations.HOME_ROUTE

  val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
  val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)

  ModalNavigationDrawer(
      drawerContent = {
        AppDrawer(
            currentRoute = currentRoute,
            navigateToHome = navController::navigateToHome,
            navigateToSettings = navController::navigateToSettings,
            closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } },
        )
      },
      drawerState = sizeAwareDrawerState,
      gesturesEnabled = !isExpandedScreen
  ) {
    Surface {
      Row {
        if (isExpandedScreen) {
          AppNavRail(
              currentRoute = currentRoute,
              navigateToHome = navController::navigateToHome,
              navigateToSettings = navController::navigateToSettings,
          )
        } else {}
        RookNavGraph(
            isExpandedScreen = isExpandedScreen,
            navController = navController,
            openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } }
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
  val drawerState = rememberDrawerState(DrawerValue.Closed)

  return if (!isExpandedScreen) {
    // If we want to allow showing the drawer, we use a real, remembered drawer
    // state defined above
    drawerState
  } else {
    // If we don't want to allow the drawer to be shown, we provide a drawer state
    // that is locked closed. This is intentionally not remembered, because we
    // don't want to keep track of any changes and always keep it closed
    DrawerState(DrawerValue.Closed)
  }
}

/** Determine the content padding to apply to the different screens of the app */
@Composable
fun rememberContentPaddingForScreen(additionalTop: Dp = 0.dp, excludeTop: Boolean = false) =
    WindowInsets.systemBars
        .only(if (excludeTop) WindowInsetsSides.Bottom else WindowInsetsSides.Vertical)
        .add(WindowInsets(top = additionalTop))
        .asPaddingValues()
