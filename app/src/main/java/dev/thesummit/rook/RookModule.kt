package dev.thesummit.rook

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.thesummit.rook.utils.NetworkStatus
import dev.thesummit.rook.utils.NetworkStatusTracker
import dev.thesummit.rook.ui.Events
import kotlin.lazy
import kotlinx.coroutines.flow.Flow
import org.chromium.net.CronetEngine

@Module
@InstallIn(SingletonComponent::class)
class RookModule() {

  private val HTTP_CACHE_SIZE = 10 * 1024 * 1024

  @Provides
  fun provideCronetEngine(@ApplicationContext context:Context): CronetEngine {
    return CronetEngine.Builder(context)
        .enableHttp2(true)
        .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, HTTP_CACHE_SIZE.toLong())
        .build()
  }

  @Provides
  fun provideNetworkStatus(@ApplicationContext context:Context): NetworkStatusTracker {
    return NetworkStatusTracker(context)
  }
}
