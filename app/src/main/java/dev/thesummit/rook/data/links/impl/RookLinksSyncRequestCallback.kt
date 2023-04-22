package dev.thesummit.rook.data.links.impl

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dev.thesummit.rook.data.RookDatabase
import dev.thesummit.rook.model.Link
import dev.thesummit.rook.model.LinkDao
import dev.thesummit.rook.workers.SyncWorker
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo

class RookLinksSyncRequestCallback(private val ctx: Context) : UrlRequest.Callback() {

  private var TAG = "RookLinksSyncRequest"
  private val bytesReceived = ByteArrayOutputStream()
  private val receiveChannel = Channels.newChannel(bytesReceived)
  private val links: LinkDao = RookDatabase.getDatabase(ctx).links()
  private val workManager = WorkManager.getInstance(ctx)

  override fun onRedirectReceived(
      request: UrlRequest?,
      info: UrlResponseInfo?,
      newLocationUrl: String?
  ) {
    request?.followRedirect()
  }

  override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
    request?.read(ByteBuffer.allocateDirect(102400))
  }

  override fun onReadCompleted(
      request: UrlRequest?,
      info: UrlResponseInfo?,
      byteBuffer: ByteBuffer?
  ) {
    // Keep reading the request until there's no more data.
    byteBuffer?.let {
      byteBuffer.flip()
      receiveChannel.write(byteBuffer)
    }
    byteBuffer?.clear()
    request?.read(byteBuffer)
  }

  override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {
    Log.i(TAG, "Sync Request successful.")

    val response = Json.parseToJsonElement(bytesReceived.toString())
    val items = response.jsonObject.get("items")?.jsonArray
    val cursor = response.jsonObject.get("cursor")?.jsonObject

    runBlocking {
      var counter = 0
      for (element: JsonElement in items.orEmpty()) {
        val link = Json.decodeFromJsonElement<Link>(element)
        links.insert(link)
        counter++
      }
      Log.i(TAG, """Synced ${counter} items from Rook server.""")
    }

    if (cursor?.get("next") != null) {
      Log.i(TAG, """Next page found with cursor: ${cursor.get("next")?.toString()}.""")
      workManager.enqueue(
          OneTimeWorkRequestBuilder<SyncWorker>()
              .setInputData(workDataOf("cursor" to cursor.get("next")?.toString()))
              .build()
      )
    }

  }

  override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException) {
    Log.e(TAG, "Sync request failed", error)
  }
}
