package dev.thesummit.rook.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.thesummit.rook.data.links.LinksRepository
import dev.thesummit.rook.data.links.impl.RookLinksRepository
import dev.thesummit.rook.data.settings.SettingsRepository
import dev.thesummit.rook.data.settings.impl.RookSettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule() {

  @Provides
  @Singleton
  fun provideLinksRepository(@ApplicationContext context: Context): LinksRepository {
    return RookLinksRepository(RookDatabase.getDatabase(context).links())
  }

  @Provides
  @Singleton
  fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
    return RookSettingsRepository(RookDatabase.getDatabase(context).settings())
  }
}
