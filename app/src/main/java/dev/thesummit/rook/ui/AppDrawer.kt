package dev.thesummit.rook.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.thesummit.rook.ui.navigation.LocalNavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import dev.thesummit.rook.R
import dev.thesummit.rook.ui.navigation.RookDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToHome: (navOptions: NavOptions?) -> Unit,
    navigateToSettings: (navOptions: NavOptions?) -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
  val navController = LocalNavController.current

  ModalDrawerSheet(modifier) {
    RookLogo(
        modifier =
            Modifier.fillMaxWidth()
                .padding(
                    horizontal = 28.dp,
                    vertical = 18.dp,
                )
    )
    NavigationDrawerItem(
        label = { Text("Home") },
        icon = { Icon(Icons.Filled.Home, null) },
        selected = currentRoute == RookDestinations.HOME_ROUTE,
        onClick = {
          navController.popBackStack(navController.graph.startDestinationId, inclusive = true)
          navigateToHome(null)
          closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
    NavigationDrawerItem(
        label = { Text("Settings") },
        icon = { Icon(Icons.Filled.Settings, null) },
        selected = currentRoute == RookDestinations.SETTINGS_ROUTE,
        onClick = {
          navigateToSettings(
              navOptions {
                popUpTo(navController.graph.startDestinationId)
              }
          )
          closeDrawer()
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
  }
}

@Composable
private fun RookLogo(modifier: Modifier = Modifier) {
  Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
    Icon(
        painterResource(R.drawable.ic_rook_logo),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp)
    )
    Spacer(Modifier.width(8.dp))
    Text(
        text = stringResource(R.string.app_name).lowercase(),
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.secondary
    )
  }
}
