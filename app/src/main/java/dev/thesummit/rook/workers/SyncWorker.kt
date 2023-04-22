package dev.thesummit.rook.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.thesummit.rook.data.RookDatabase
import dev.thesummit.rook.data.links.impl.RookLinksSyncRequestCallback
import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.model.SettingKey
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest

class SyncWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

  private val TAG = "RookSyncWorker"
  private val rookDatabase = RookDatabase.getDatabase(applicationContext)
  private val PAGE_LIMIT = 100
  private val HTTP_CACHE_SIZE = 10 * 1024 * 1024

  override suspend fun doWork(): Result {

    val cursor = inputData.getString("cursor")

    Log.i(TAG, "Beginning local Rook database sync with server.")
    if (cursor != null) {
      Log.i(TAG, """Resuming sync with cursor: ${cursor}""")
    }

    rookDatabase
        .settings()
        .insert(Setting(key = SettingKey.HOST.key, value = "https://rook.thesummit.dev"))

    val apiKey: Setting? = rookDatabase.settings().get(SettingKey.API_KEY.key)
    val host: Setting? = rookDatabase.settings().get(SettingKey.HOST.key)

    if (apiKey == null) {
      Log.e(TAG, "Could not find ApiKey to issue request.")
      return Result.failure()
    }

    if (host == null) {
      Log.e(TAG, "Could not find host setting to issue request.")
      return Result.failure()
    }

    return withContext(Dispatchers.IO) {
      // TODO: Inject this once Hilt is added.
      val cronetEngine =
          CronetEngine.Builder(applicationContext)
              .enableHttp2(true)
              .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, HTTP_CACHE_SIZE.toLong())
              .build()

      val executor: Executor = Executors.newSingleThreadExecutor()
      val data =
          if (cursor !== null) """{"limit":${PAGE_LIMIT}, "cursor":${cursor}}"""
          else """{"limit":${PAGE_LIMIT}}"""
      val uploadDataProvider = UploadDataProviders.create(data.toByteArray())
      val requestBuilder =
          cronetEngine
              .newUrlRequestBuilder(
                  """${host.value}/links""",
                  RookLinksSyncRequestCallback(ctx),
                  executor
              )
              .addHeader("Authorization", """Bearer ${apiKey.value}""")
              .addHeader("Content-Type", "application/json")
              .setHttpMethod("POST")
              .setUploadDataProvider(uploadDataProvider, executor)
      val request: UrlRequest = requestBuilder.build()
      request.start()

      Result.success()
    }
  }
}
