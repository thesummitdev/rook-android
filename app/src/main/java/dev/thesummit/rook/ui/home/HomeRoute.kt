package dev.thesummit.rook.ui.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
) {

  // Generate the uiState.
  val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()

  HomeRoute(
      uiState = uiState,
      isExpandedScreen = isExpandedScreen,
      openDrawer = openDrawer,
      onRefreshLinks = { homeViewModel.refreshLinks() }
  )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    onRefreshLinks: () -> Unit,
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
