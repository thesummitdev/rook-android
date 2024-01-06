package dev.thesummit.rook.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.thesummit.rook.R
import dev.thesummit.rook.ui.navigation.RookDestinations

@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToCreate: () -> Unit,
    modifier: Modifier = Modifier
) {
  NavigationRail(
      header = {
        Icon(
            painter = painterResource(R.drawable.ic_rook_logo),
            contentDescription = "navigate",
            tint = MaterialTheme.colorScheme.primary,
        )
      },
      modifier = modifier
  ) {
    Spacer(Modifier.weight(1f))
    NavigationRailItem(
        modifier = Modifier.padding(6.dp),
        selected = currentRoute == RookDestinations.HOME_ROUTE,
        onClick = navigateToHome,
        icon = { Icon(Icons.Filled.Home, stringResource(R.string.home_title)) },
        label = { Text(stringResource(R.string.home_title)) },
        alwaysShowLabel = false
    )
    NavigationRailItem(
        modifier = Modifier.padding(6.dp),
        selected = currentRoute == RookDestinations.CREATE_ROUTE,
        onClick = navigateToCreate,
        icon = { Icon(Icons.Filled.Add, stringResource(R.string.create_title)) },
        label = { Text(stringResource(R.string.settings_title)) },
        alwaysShowLabel = false
    )
    NavigationRailItem(
        modifier = Modifier.padding(6.dp),
        selected = currentRoute == RookDestinations.SETTINGS_ROUTE,
        onClick = navigateToSettings,
        icon = { Icon(Icons.Filled.Settings, stringResource(R.string.settings_title)) },
        label = { Text(stringResource(R.string.settings_title)) },
        alwaysShowLabel = false
    )
    Spacer(Modifier.weight(1f))
  }
}
