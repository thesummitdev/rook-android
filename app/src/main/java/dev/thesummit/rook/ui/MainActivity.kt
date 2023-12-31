package dev.thesummit.rook.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.thesummit.rook.ui.navigation.LocalNavController
import dev.thesummit.rook.ui.navigation.Navigator
import dev.thesummit.rook.ui.navigation.setupWithNavController
import dev.thesummit.rook.ui.theme.RookTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject lateinit var navigator: Navigator

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    setContent {
      val navController = rememberNavController()
      navigator.setupWithNavController(this, navController)

      RookTheme {
        val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
        CompositionLocalProvider(LocalNavController provides navController) {
          RookApp(widthSizeClass)
        }
      }
    }
  }
}
