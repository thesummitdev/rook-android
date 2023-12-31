package dev.thesummit.rook.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Label
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import dev.thesummit.rook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
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
      actions = {
        IconButton(onClick = { /* TODO: Open filters */}) {
          Icon(imageVector = Icons.Filled.Label, contentDescription = "search")
        }
        IconButton(onClick = { /* TODO: Open search */}) {
          Icon(imageVector = Icons.Filled.Search, contentDescription = "search")
        }
      },
      scrollBehavior = scrollBehavior,
      modifier = modifier
  )
}
