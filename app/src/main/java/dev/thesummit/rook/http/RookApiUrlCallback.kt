package dev.thesummit.rook.http

import android.util.Log
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.chromium.net.CronetException
import org.chromium.net.UrlRequest
import org.chromium.net.UrlResponseInfo

class RookApiUrlRequestCallback(val onDataReady: suspend (json: JsonElement) -> Unit) :
    UrlRequest.Callback() {

  private val bytesReceived = ByteArrayOutputStream()
  private val receiveChannel = Channels.newChannel(bytesReceived)

  companion object {
    val TAG = "RookApi"
    private val BYTE_BUFFER_CAPACITY_BYTES = 16_000
  }

  override fun onSucceeded(request: UrlRequest?, info: UrlResponseInfo?) {

    if (info?.httpStatusCode == 200) {
      val response = Json.parseToJsonElement(bytesReceived.toString())
      Log.d(TAG, """Response received: ${response}""")

      runBlocking(Dispatchers.IO) { onDataReady(response) }
    } else {
      Log.e(
          TAG,
          "API request returned non-OK status code: ${info?.httpStatusCode}:${info?.httpStatusText}"
      )
    }
  }

  override fun onRedirectReceived(
      request: UrlRequest?,
      info: UrlResponseInfo?,
      newLocationUrl: String?
  ) {
    request?.followRedirect()
  }

  override fun onResponseStarted(request: UrlRequest?, info: UrlResponseInfo?) {
    request?.read(ByteBuffer.allocateDirect(BYTE_BUFFER_CAPACITY_BYTES))
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

  override fun onFailed(request: UrlRequest?, info: UrlResponseInfo?, error: CronetException) {
    Log.e(TAG, "Login request failed", error)
  }
}
