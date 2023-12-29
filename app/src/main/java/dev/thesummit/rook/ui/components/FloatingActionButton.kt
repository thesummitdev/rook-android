package dev.thesummit.rook.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add

@Composable
fun RookFloatingActionButton(onClick: ()->Unit){

  SmallFloatingActionButton(onClick = { onClick() },
    containerColor = MaterialTheme.colorScheme.secondaryContainer,
    contentColor = MaterialTheme.colorScheme.secondary,
  ) {
    Icon(Icons.Filled.Add, null)
  }

}
