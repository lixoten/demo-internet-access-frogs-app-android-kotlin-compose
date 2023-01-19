package com.example.frogs

import android.app.Application
import com.example.frogs.di.AppContainer
import com.example.frogs.di.DefaultAppContainer

class FrogsApplication: Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}