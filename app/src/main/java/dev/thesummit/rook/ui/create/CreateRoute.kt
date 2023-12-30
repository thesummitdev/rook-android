package dev.thesummit.rook.ui.create

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue

@Composable
fun CreateRoute(
    openDrawer: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel()
) {
  CreatePage(openDrawer = openDrawer)
}
