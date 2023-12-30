package dev.thesummit.rook.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.thesummit.rook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePage(
    openDrawer: () -> Unit,
    viewModel: CreateViewModel = hiltViewModel(),
) {
  val topAppBarState = rememberTopAppBarState()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
      topBar = {
        CreateTopAppBar(
            openDrawer = openDrawer,
            topAppBarState = topAppBarState,
        )
      },
  ) { innerPadding ->
    val contentModifier = Modifier.padding(innerPadding).fillMaxWidth()

    Column(
        modifier = contentModifier,

        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
      Row { Text(text = "Add a new link", style = MaterialTheme.typography.titleLarge) }

      TextField(
          modifier = Modifier.fillMaxWidth(),
          value = viewModel.formState.title,
          isError = !viewModel.formState.invalidFields.contains(LinkForm.Fields.TITLE),
          supportingText = {
            if (viewModel.formState.invalidFields.contains(LinkForm.Fields.TITLE))
                Text(viewModel.formState.titleErrorMessage)
          },
          onValueChange = { viewModel.onEvent(CreateUiEvent.TitleChanged(it)) },
          label = { Text("Title") },
      )

      TextField(
          modifier = Modifier.fillMaxWidth(),
          value = viewModel.formState.url,
          isError = !viewModel.formState.invalidFields.contains(LinkForm.Fields.URL),
          supportingText = {
            if (viewModel.formState.invalidFields.contains(LinkForm.Fields.URL))
                Text(viewModel.formState.urlErrorMessage)
          },
          onValueChange = { viewModel.onEvent(CreateUiEvent.UrlChanged(it)) },
          label = { Text("Url") },
      )

      TextField(
          modifier = Modifier.fillMaxWidth(),
          value = viewModel.formState.tags,
          onValueChange = { viewModel.onEvent(CreateUiEvent.TagsChanged(it)) },
          label = { Text("Tags") },
      )

      Button(
          onClick = { viewModel.onEvent(CreateUiEvent.Submit) },
          content = { Text("Create") },
          enabled =
              // No validation errors
              viewModel.formState.invalidFields.isEmpty() &&
                  // Required fields are not empty
                  viewModel.formState.title.isNotEmpty() &&
                  viewModel.formState.url.isNotEmpty()
      )

      if (uiState.requestPending) {
        LinearProgressIndicator(modifier = Modifier.width(64.dp))
      }
    }
  }
}

/** Top App bar for the settings view */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTopAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState),
) {
  TopAppBar(
      title = {},
      navigationIcon = {
        IconButton(onClick = openDrawer) {
          Icon(
              painter = painterResource(R.drawable.ic_rook_logo),
              contentDescription = "navigate",
              tint = MaterialTheme.colorScheme.primary,
          )
        }
      },
      actions = {},
      scrollBehavior = scrollBehavior,
      modifier = modifier,
  )
}
