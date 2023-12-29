package dev.thesummit.rook.ui.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

import dev.thesummit.rook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePage(
    openDrawer: () -> Unit,
) {

  val topAppBarState = rememberTopAppBarState()

  Scaffold(
      topBar = {
        CreateTopAppBar(
            openDrawer = openDrawer,
            topAppBarState = topAppBarState,
        )
      }
  ) { innerPadding ->
    val contentModifier = Modifier.padding(innerPadding)

    Column(modifier = contentModifier) { Text("create page") }
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
      actions = {},
      scrollBehavior = scrollBehavior,
      modifier = modifier
  )
}
