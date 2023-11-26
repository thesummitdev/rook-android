package dev.thesummit.rook.data

import android.content.Context
import android.app.Application
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.data.links.impl.RookLinksRepository
import dev.thesummit.rook.data.settings.SettingsRepository
import dev.thesummit.rook.data.settings.impl.RookSettingsRepository
import kotlin.lazy
import org.chromium.net.CronetEngine

/** Dependency Injection container at the application level. */
interface AppContainer {
  val linksRepository: LinksRepository
  val settingsRepository: SettingsRepository
  val cronetEngine: CronetEngine
  val applicationContext: Context
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(override val applicationContext: Context) : AppContainer {

  private val HTTP_CACHE_SIZE =  10 * 1024 * 1024
  // For a fake test repository that returns a static list and sometimes flakes.
  // override val linksRepository: LinksRepository by lazy { FakeLinksRepository() }
  // For a database backed repository:
  override val linksRepository: LinksRepository by lazy {
    RookLinksRepository(RookDatabase.getDatabase(applicationContext).links(), cronetEngine)
  }
  override val settingsRepository: SettingsRepository by lazy {
    RookSettingsRepository(RookDatabase.getDatabase(applicationContext).settings())
  }
  override val cronetEngine: CronetEngine by lazy {
    CronetEngine.Builder(applicationContext)
        .enableHttp2(true)
        .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, HTTP_CACHE_SIZE.toLong())
        .build()
  }
}
