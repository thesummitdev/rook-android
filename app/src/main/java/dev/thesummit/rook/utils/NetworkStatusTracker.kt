package dev.thesummit.rook.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.callbackFlow

sealed class NetworkStatus {
  object Available : NetworkStatus()
  object Unavailable : NetworkStatus()
}

class NetworkStatusTracker(context: Context) {

  companion object {
    val TAG = "RookNetworkStatusTracker"
  }

  private val connectivityManager: ConnectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  val networkStatus =
      callbackFlow<NetworkStatus> {
        val networkStatusCallback =
            object : ConnectivityManager.NetworkCallback() {
              override fun onUnavailable() {
                trySend(NetworkStatus.Unavailable).onFailure {
                  Log.i(TAG, """Failed to notify downstream onUnavailable.""")
                }
              }
              override fun onAvailable(network: Network) {
                trySend(NetworkStatus.Available).onFailure {
                  Log.i(TAG, """Failed to notify downstream onAvailable.""")
                }
              }
              override fun onLost(network: Network) {
                trySend(NetworkStatus.Unavailable).onFailure {
                  Log.i(TAG, """Failed to notify downstream onLost.""")
                }
              }
            }

        val request =
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

        connectivityManager.registerNetworkCallback(request, networkStatusCallback)

        awaitClose { connectivityManager.unregisterNetworkCallback(networkStatusCallback) }
      }
}
