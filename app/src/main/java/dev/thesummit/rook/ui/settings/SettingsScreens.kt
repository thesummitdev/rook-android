package dev.thesummit.rook.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.thesummit.rook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(uiState: SettingsUiState) {
  when (uiState) {
    is SettingsUiState.isInitialized -> SettingsInitialized(uiState)
    is SettingsUiState.NotInitialized -> SettingsLogin(uiState)
  }
}

/** The logged in view for the settings page. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsInitialized(uiState: SettingsUiState.isInitialized) {

  Column {
    Column(modifier = Modifier.padding(10.dp)) {
      Text(
          text = stringResource(R.string.label_settings_hostname),
          style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.padding(8.dp))
      Text(text = uiState.host.value, style = MaterialTheme.typography.labelLarge)
    }

    Column(modifier = Modifier.padding(10.dp)) {
      Text(
          text = stringResource(R.string.label_settings_apikey),
          style = MaterialTheme.typography.titleMedium
      )
      Spacer(modifier = Modifier.padding(8.dp))
      Text(text = uiState.apiKey.value, style = MaterialTheme.typography.labelSmall)
    }
  }
}

/** The login view when the user is not logged in. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLogin(uiState: SettingsUiState.NotInitialized) {

  val viewModel: SettingsViewModel = viewModel()

  /** Verify if all strings are non empty */
  fun areAllNotEmpty(vararg strings: String): Boolean {
    return strings.all { it.isNotEmpty() }
  }

  var host by rememberSaveable { mutableStateOf("") }
  var username by rememberSaveable { mutableStateOf("") }
  var password by rememberSaveable { mutableStateOf("") }

  Surface {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
      TextField(
          value = host,
          onValueChange = { host = it },
          placeholder = { Text(text = stringResource(R.string.label_server_host)) }
      )
      Spacer(Modifier.padding(8.dp))
      TextField(
          value = username,
          onValueChange = { username = it },
          placeholder = { Text(text = stringResource(R.string.label_username)) }
      )
      Spacer(Modifier.padding(8.dp))
      TextField(
          value = password,
          onValueChange = { password = it },
          placeholder = { Text(text = stringResource(R.string.label_password)) }
      )
      Spacer(Modifier.padding(8.dp))
      Button(
          onClick = { viewModel.attemptToSignIn(host, username, password) },
          enabled = areAllNotEmpty(host, username, password)
      ) { Text(text = stringResource(R.string.label_sign_in_button)) }
    }
  }
}
