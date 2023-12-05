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
import dev.thesummit.rook.model.Setting
import dev.thesummit.rook.model.SettingKey
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.requireNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import org.chromium.net.CronetEngine
import org.chromium.net.UploadDataProviders
import org.chromium.net.UrlRequest

enum class WorkerAction {
  LOGIN,
  CREATE_API_KEY
}

/** A worker that logs into the Rook API and creates a persistent API token. */
class LoginWorker(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

  companion object {
    private val TAG = "RookLoginWorker"
    private val HTTP_CACHE_SIZE = 10 * 1024 * 1024
  }

  private val rookDatabase = RookDatabase.getDatabase(applicationContext)
  private val workManager = WorkManager.getInstance(ctx)
  private val serverAddress: String =
      requireNotNull(inputData.getString("server_address")) { "server_address must not be null." }
  private val username: String =
      requireNotNull(inputData.getString("username")) { "username must not be null" }
  private val password: String =
      requireNotNull(inputData.getString("password")) { "password must not be null" }
  private val jwt: String? = inputData.getString("jwt")

  override suspend fun doWork(): Result {

    val workerAction = if (jwt == null) WorkerAction.LOGIN else WorkerAction.CREATE_API_KEY

    return withContext(Dispatchers.IO) {
      // TODO inject this
      val cronetEngine =
          CronetEngine.Builder(applicationContext)
              .enableHttp2(true)
              .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_IN_MEMORY, HTTP_CACHE_SIZE.toLong())
              .build()

      val executor: Executor = Executors.newSingleThreadExecutor()
      val uploadDataProvider = UploadDataProviders.create(getBytesForAction(workerAction))

      val requestBuilder =
          cronetEngine
              .newUrlRequestBuilder(
                  getPathforAction(workerAction),
                  RookApiUrlRequestCallback(::onDataReady),
                  executor,
              )
              .addHeader("Content-Type", "application/json")
              .setHttpMethod(getHttpVerbForAction(workerAction))
              .setUploadDataProvider(uploadDataProvider, executor)

      if (jwt != null) {
        // Include the jwt in the request headers.
        requestBuilder.addHeader("Authorization", """Bearer ${jwt}""")
      }

      val request: UrlRequest = requestBuilder.build()
      request.start()

      Result.success()
    }
  }

  private suspend fun getBytesForAction(action: WorkerAction): ByteArray =
      when (action) {
        WorkerAction.CREATE_API_KEY -> "".toByteArray()
        WorkerAction.LOGIN ->
            """{"username":"${username}", "password": "${password}"}""".toByteArray()
      }

  private suspend fun getHttpVerbForAction(action: WorkerAction): String =
      when (action) {
        WorkerAction.CREATE_API_KEY -> "GET"
        WorkerAction.LOGIN -> "POST"
      }
  private suspend fun getPathforAction(action: WorkerAction): String =
      when (action) {
        WorkerAction.CREATE_API_KEY -> """${serverAddress}/users/apikey/new"""
        WorkerAction.LOGIN -> """${serverAddress}/login"""
      }

  /**
   * Call back that will be invoked by Cronet when the JSON response from the server is available.
   * @return
   */
  suspend fun onDataReady(response: JsonElement) {

    // Json library seems to wrap these strings in double quotes, so remove them if they exist.
    val jwt = response.jsonObject.get("jwt")?.toString()?.removeSurrounding("\"", "\"")
    val apiKey = response.jsonObject.get("apiKey")?.toString()?.removeSurrounding("\"", "\"")

    // This was the first successful login, now we need to send another request
    // and generate a permanent API key.
    if (jwt != null) {
      Log.i(TAG, "Token was correctly fetched, will attempt to create an API key.")
      workManager.enqueue(
          OneTimeWorkRequestBuilder<LoginWorker>()
              .setInputData(
                  workDataOf(
                      "server_address" to serverAddress,
                      "username" to username,
                      "password" to password,
                      "jwt" to jwt,
                  )
              )
              .build()
      )
      return
    } else if (apiKey != null) {
      val apiKeySetting: Setting = Setting(key = SettingKey.API_KEY.key, value = apiKey)
      val hostSetting: Setting = Setting(key = SettingKey.HOST.key, value = serverAddress)
      rookDatabase.settings().insert(apiKeySetting)
      rookDatabase.settings().insert(hostSetting)
      return
    }

    Log.e(
        TAG,
        """Request succeeded, but missing required login data. Response: ${response.toString()}"""
    )
  }
}
