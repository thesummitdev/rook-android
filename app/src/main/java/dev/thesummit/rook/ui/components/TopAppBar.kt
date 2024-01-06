package dev.thesummit.rook.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.thesummit.rook.R
import dev.thesummit.rook.ui.navigation.LocalNavController
import dev.thesummit.rook.ui.navigation.RookDestinations
import dev.thesummit.rook.ui.settings.SettingsUiState
import dev.thesummit.rook.ui.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    isExpandedScreen: Boolean = false,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
) {

  val navController = LocalNavController.current
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route ?: RookDestinations.HOME_ROUTE

  TopAppBar(
      title = {},
      navigationIcon = {
        if (!isExpandedScreen) {
          IconButton(onClick = openDrawer) {
            Icon(
                painter = painterResource(R.drawable.ic_rook_logo),
                contentDescription = "navigate",
                tint = MaterialTheme.colorScheme.primary,
            )
          }
        }
      },
      actions = {
        if (currentRoute == RookDestinations.HOME_ROUTE) {
          IconButton(onClick = { /* TODO: Open filters */}) {
            Icon(imageVector = Icons.Filled.Label, contentDescription = "search")
          }
          IconButton(onClick = { /* TODO: Open search */}) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
          }
        }
        if (currentRoute == RookDestinations.SETTINGS_ROUTE) {

          val settingsViewModel: SettingsViewModel = hiltViewModel()
          val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
          val isLoggedIn =
              when (settingsState) {
                is SettingsUiState.isInitialized -> true
                is SettingsUiState.NotInitialized -> false
              }
          if (isLoggedIn) {
            IconButton(onClick = settingsViewModel::logout) {
              Icon(imageVector = Icons.Filled.Logout, contentDescription = "logout")
            }
          }
        }
      },
      scrollBehavior = scrollBehavior,
      modifier = modifier
  )
}
