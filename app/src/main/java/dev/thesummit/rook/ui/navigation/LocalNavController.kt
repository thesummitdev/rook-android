package dev.thesummit.rook.ui.navigation

import androidx.navigation.NavHostController
import androidx.compose.runtime.compositionLocalOf


val LocalNavController = compositionLocalOf<NavHostController> {
  error("No LocalNavController provided")
}
