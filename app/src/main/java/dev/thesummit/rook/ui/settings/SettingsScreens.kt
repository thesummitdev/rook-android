package dev.thesummit.rook.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.thesummit.rook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(uiState: SettingsUiState, isExpandedScreen: Boolean, openDrawer: () -> Unit) {

  val topAppBarState = rememberTopAppBarState()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
  val viewModel: SettingsViewModel = viewModel()

  val isLoggedIn: Boolean =
      when (uiState) {
        is SettingsUiState.isInitialized -> true
        is SettingsUiState.NotInitialized -> false
      }

  Scaffold(
      topBar = {
        SettingsTopAppBar(
            openDrawer = openDrawer,
            topAppBarState = topAppBarState,
            isLoggedIn = isLoggedIn,
            onLogout = viewModel::logout,
        )
      }
  ) { innerPadding ->
    val contentModifier =
        Modifier.padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxWidth()

    Column(modifier = contentModifier) {
      when (uiState) {
        is SettingsUiState.isInitialized -> SettingsInitialized(uiState, isExpandedScreen)
        is SettingsUiState.NotInitialized -> SettingsLogin(uiState, isExpandedScreen)
      }
    }
  }
}

/** The logged in view for the settings page. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsInitialized(
    uiState: SettingsUiState.isInitialized,
    isExpandedScreen: Boolean,
) {

  Column {
    Text("""host: ${uiState.host.value}""")
    Text("""apiKey: ${uiState.apiKey.value}""")
  }
}

/** The login view when the user is not logged in. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLogin(
    uiState: SettingsUiState.NotInitialized,
    isExpandedScreen: Boolean,
) {

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

/** Top App bar for the settings view */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(
    openDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean = false,
    onLogout: () -> Unit,
    topAppBarState: TopAppBarState = rememberTopAppBarState(),
    scrollBehavior: TopAppBarScrollBehavior? =
        TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
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
      actions = {
        if (isLoggedIn) {
          IconButton(onClick = onLogout) {
            Icon(imageVector = Icons.Filled.Logout, contentDescription = "logout")
          }
        }
      },
      scrollBehavior = scrollBehavior,
      modifier = modifier
  )
}
