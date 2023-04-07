package dev.thesummit.rook.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.thesummit.rook.data.AppContainer
import dev.thesummit.rook.ui.home.HomeRoute
import dev.thesummit.rook.ui.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RookNavGraph(
    appContainer: AppContainer,
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
    composable(RookDestinations.INITIAL_ROUTE) {
      Surface {
        Text(
            "Hello intial route",
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 24.dp)
        )
      }
    }

    composable(RookDestinations.HOME_ROUTE) {
      val homeViewModel: HomeViewModel =
          viewModel(factory = HomeViewModel.provideFactory(appContainer.linksRepository))
      HomeRoute(
          homeViewModel = homeViewModel,
          isExpandedScreen = isExpandedScreen,
          openDrawer = openDrawer
      )
    }
  }
}
