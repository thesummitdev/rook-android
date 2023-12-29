package dev.thesummit.rook.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import android.util.Log

private enum class SettingsScreenType {
  INITIALIZED,
  LOGIN
}

@Composable
fun SettingsRoute(
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {


  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  SettingsPage(uiState, isExpandedScreen,openDrawer)
}
