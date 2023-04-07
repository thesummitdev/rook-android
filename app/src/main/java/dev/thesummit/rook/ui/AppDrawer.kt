package dev.thesummit.rook.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.thesummit.rook.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToInit: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {

  ModalDrawerSheet(modifier) {
    RookLogo(
      modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
    )
    NavigationDrawerItem(
        label = { Text("Init") },
        icon = { Icon(Icons.Filled.Settings, null) },
        selected = currentRoute == RookDestinations.INITIAL_ROUTE,
        onClick = {
          navigateToInit()
          closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        label = { Text("Home") },
        icon = { Icon(Icons.Filled.Home, null) },
        selected = currentRoute == RookDestinations.HOME_ROUTE,
        onClick = {
          navigateToHome()
          closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
  }
}

@Composable
private fun RookLogo(modifier: Modifier = Modifier) {
  Row(modifier = modifier) {
    Icon(
        painterResource(R.drawable.ic_rook_logo),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.width(8.dp))
    Text(text = stringResource(R.string.app_name))
  }
}
