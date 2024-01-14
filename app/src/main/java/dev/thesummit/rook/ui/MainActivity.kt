package dev.thesummit.rook.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.thesummit.rook.ui.navigation.LocalNavController
import dev.thesummit.rook.ui.navigation.Navigator
import dev.thesummit.rook.ui.navigation.RookDestinations
import dev.thesummit.rook.ui.navigation.setupWithNavController
import dev.thesummit.rook.ui.theme.RookTheme
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject lateinit var navigator: Navigator
  @Inject lateinit var events: Events

  @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    val route = parseRouteFromIntent(getIntent())

    setContent {
      val coroutineScope = rememberCoroutineScope()
      val navController = rememberNavController()
      navigator.setupWithNavController(this, navController)

      RookTheme {
        val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
        CompositionLocalProvider(LocalNavController provides navController) {
          RookApp(
              route,
              widthSizeClass,
              toggleSearch = { coroutineScope.launch { events.sendEvent(UiEvent.ToggleSearch)} },
          )
        }
      }
    }
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    navigator.navigateTo(parseRouteFromIntent(intent))
  }

  /**
   * Generates the route to navigate to based on the intent that started the activity.
   *
   * @return the route the app should begin at.
   */
  private fun parseRouteFromIntent(intent: Intent): String {
    return when (intent.getAction()) {
      Intent.ACTION_SEND -> {
        val extraText: String? = getIntent().getStringExtra(Intent.EXTRA_TEXT)
        if (extraText != null && Patterns.WEB_URL.matcher(extraText).matches()) {
          // we have a valid url
          """${RookDestinations.CREATE_ROUTE}?url=${extraText}"""
        } else {
          RookDestinations.HOME_ROUTE
        }
      }
      else -> RookDestinations.HOME_ROUTE
    }
  }
}
