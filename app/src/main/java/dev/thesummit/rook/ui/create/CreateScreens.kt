package dev.thesummit.rook.ui.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.thesummit.rook.R
import dev.thesummit.rook.ui.components.AutocompleteTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePage(
    prefillUrl: String? = null,
    viewModel: CreateViewModel = hiltViewModel(),
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val focusRequester = remember { FocusRequester() }

  // When a prefillUrl is provided, dispatch an event to update the url text field.
  LaunchedEffect(prefillUrl) {
    if (!prefillUrl.isNullOrEmpty()) {
      viewModel.onEvent(CreateUiEvent.UrlChanged(prefillUrl))

      // Set focus to the title field so the user can begin
      focusRequester.requestFocus()
    }
  }

  Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp),
  ) {
    Row {
      Text(
          text = stringResource(R.string.create_page_title),
          style = MaterialTheme.typography.titleLarge
      )
    }

    TextField(
        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
        value = viewModel.formState.title,
        isError = viewModel.formState.invalidFields.contains(LinkForm.Fields.TITLE),
        supportingText = {
          if (viewModel.formState.invalidFields.contains(LinkForm.Fields.TITLE))
              Text(viewModel.formState.titleErrorMessage)
        },
        onValueChange = { viewModel.onEvent(CreateUiEvent.TitleChanged(it)) },
        label = { Text(text = stringResource(R.string.create_page_title_label)) },
    )

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = viewModel.formState.url,
        isError = viewModel.formState.invalidFields.contains(LinkForm.Fields.URL),
        supportingText = {
          if (viewModel.formState.invalidFields.contains(LinkForm.Fields.URL))
              Text(viewModel.formState.urlErrorMessage)
        },
        onValueChange = { viewModel.onEvent(CreateUiEvent.UrlChanged(it)) },
        label = { Text(text = stringResource(R.string.create_page_url_label)) },
    )

    AutocompleteTextField(
        modifier = Modifier.fillMaxWidth(),
        predictions = viewModel.tagPredictions,
        value = viewModel.formState.tags,
        onValueChange = { viewModel.onEvent(CreateUiEvent.TagsChanged(it)) },
        label = { Text(text = stringResource(R.string.create_page_tags_label)) },
    )

    Button(
        onClick = { viewModel.onEvent(CreateUiEvent.Submit) },
        content = { Text(text = stringResource(R.string.create_page_create_button_label)) },
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
