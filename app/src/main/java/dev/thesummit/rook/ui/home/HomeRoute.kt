package dev.thesummit.rook.ui.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.thesummit.rook.ui.navigation.navigateToCreate

@Composable
fun HomeRoute(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

  // Generate the uiState.
  val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

  HomeRoute(
      uiState = uiState,
      isExpandedScreen = isExpandedScreen,
      openDrawer = openDrawer,
      onRefreshLinks = homeViewModel::refreshLinks,
      onFabClick = navController::navigateToCreate,
  )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    onRefreshLinks: () -> Unit,
    onFabClick: () -> Unit,
) {

  val homeListLazyListState = rememberLazyListState()

  val homeScreenType = getHomeScreenType(uiState)

  when (homeScreenType) {
    HomeScreenType.LinksFeed -> {
      HomeLinksFeed(
          uiState = uiState,
          showTopAppBar = !isExpandedScreen,
          openDrawer = openDrawer,
          lazyListState = homeListLazyListState,
          onRefreshLinks = onRefreshLinks,
          onFabClick = onFabClick,
      )
    }
  }
}

private enum class HomeScreenType {
  LinksFeed
}

@Composable
private fun getHomeScreenType(uiState: HomeUiState): HomeScreenType =
    when (uiState) {
      is HomeUiState.HasLinks -> HomeScreenType.LinksFeed
      is HomeUiState.NoLinks -> HomeScreenType.LinksFeed
    }
