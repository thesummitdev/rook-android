package dev.thesummit.rook;

import android.app.Application
import dev.thesummit.rook.data.AppContainer
import dev.thesummit.rook.data.AppContainerImpl


class RookApplication : Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl(this)
    }
}
