package dev.thesummit.rook.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.painterResource
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import dev.thesummit.rook.R
import dev.thesummit.rook.ui.navigation.RookDestinations
import android.util.Log


@Composable
fun AppNavRail(
    currentRoute: String,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        header = {
            Icon(
                painterResource(R.drawable.ic_rook_logo),
                null,
                Modifier.padding(vertical = 12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        modifier = modifier
    ) {
        Spacer(Modifier.weight(1f))
        NavigationRailItem(
            selected = currentRoute == RookDestinations.HOME_ROUTE,
            onClick = navigateToHome,
            icon = { Icon(Icons.Filled.Home, stringResource(R.string.home_title)) },
            label = { Text(stringResource(R.string.home_title)) },
            alwaysShowLabel = false
        )
        NavigationRailItem(
            selected = currentRoute == RookDestinations.SETTINGS_ROUTE,
            onClick = navigateToSettings,
            icon = { Icon(Icons.Filled.ListAlt, stringResource(R.string.settings_title)) },
            label = { Text(stringResource(R.string.settings_title)) },
            alwaysShowLabel = false
        )
        Spacer(Modifier.weight(1f))
    }
}

