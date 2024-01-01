package dev.thesummit.rook.ui.home

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

  // Generate the uiState.
  val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
  val lazyListState = rememberLazyListState()

  HomeRoute(
      uiState = uiState,
      lazyListState = lazyListState,
      onRefreshLinks = homeViewModel::refreshLinks,
  )
}

@Composable
fun HomeRoute(
    uiState: HomeUiState,
    lazyListState: LazyListState,
    onRefreshLinks: () -> Unit,
) {
  val homeScreenType = getHomeScreenType(uiState)

  when (homeScreenType) {
    HomeScreenType.LinksFeed -> {
      HomeLinksFeed(
          uiState = uiState,
          lazyListState = lazyListState,
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
