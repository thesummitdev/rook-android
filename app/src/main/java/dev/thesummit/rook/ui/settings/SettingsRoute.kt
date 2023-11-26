package dev.thesummit.rook.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.util.Log

private enum class SettingsScreenType {
  INITIALIZED,
  LOGIN
}

@Composable
fun SettingsRoute(
    viewModel: SettingsViewModel,
    isExpandedScreen: Boolean,
    openDrawer: () -> Unit,
) {


  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  SettingsPage(uiState, isExpandedScreen,openDrawer)
}
