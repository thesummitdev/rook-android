package dev.thesummit.rook.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dev.thesummit.rook.data.RookDatabase
import dev.thesummit.rook.http.RookApiUrlRequestCallback
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.model.SettingKey
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest

class SyncWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

  companion object {
    private val TAG = "RookSyncWorker"
    private val HTTP_CACHE_SIZE = 10 * 1024 * 1024
    private val PAGE_LIMIT = 100
  }

  private val rookDatabase = RookDatabase.getDatabase(applicationContext)
  private val workManager = WorkManager.getInstance(ctx)

  override suspend fun doWork(): Result {

    val cursor = inputData.getString("cursor")

    Log.i(TAG, "Beginning local Rook database sync with server.")
    if (cursor != null) {
      Log.i(TAG, """Resuming sync with cursor: ${cursor}""")
    }

    val apiKey: Setting? = rookDatabase.settings().getOnce(SettingKey.API_KEY.key)
    val host: Setting? = rookDatabase.settings().getOnce(SettingKey.HOST.key)

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
                  RookApiUrlRequestCallback(::onDataReady),
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

  /**
   * Call back that will be invoked by Cronet when the JSON response from the server is available.
   */
  suspend fun onDataReady(response: JsonElement) {

    Log.i(TAG, "Sync Request successful.")

    val items = response.jsonObject.get("items")?.jsonArray
    val cursor = response.jsonObject.get("cursor")?.jsonObject

    var counter = 0
    val links = rookDatabase.links()
    for (element: JsonElement in items.orEmpty()) {
      val link = Json.decodeFromJsonElement<Link>(element)
      links.insert(link)
      counter++
    }
    Log.i(TAG, """Synced ${counter} items from Rook server.""")

    if (cursor?.get("next") != null) {
      Log.i(TAG, """Next page found with cursor: ${cursor.get("next")?.toString()}.""")
      workManager.enqueue(
          OneTimeWorkRequestBuilder<SyncWorker>()
              .setInputData(workDataOf("cursor" to cursor.get("next")?.toString()))
              .build()
      )
    }
  }
}
