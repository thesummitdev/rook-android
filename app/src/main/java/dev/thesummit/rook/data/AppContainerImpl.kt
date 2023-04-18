package dev.thesummit.rook.data

import android.content.Context
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.data.links.impl.RookLinksRepository
import dev.thesummit.rook.data.links.impl.FakeLinksRepository
import kotlin.lazy

/** Dependency Injection container at the application level. */
interface AppContainer {
  val linksRepository: LinksRepository
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class AppContainerImpl(private val applicationContext: Context) : AppContainer {
  // For a fake test repository that returns a static list and sometimes flakes.
  override val linksRepository: LinksRepository by lazy { FakeLinksRepository() }
  // For a database backed repository:
  // override val linksRepository: LinksRepository by lazy {
  //   RookLinksRepository(RookDatabase.getDatabase(applicationContext).linkDao())
  // }
}
