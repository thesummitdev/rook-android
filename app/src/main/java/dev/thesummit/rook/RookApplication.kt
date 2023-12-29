package dev.thesummit.rook

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RookApplication : Application() {
  override fun onCreate() {
    super.onCreate()
  }
}
