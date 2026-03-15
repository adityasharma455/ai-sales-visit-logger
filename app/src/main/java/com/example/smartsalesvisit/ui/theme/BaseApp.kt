package com.example.smartsalesvisit.ui.theme

import android.app.Application
import com.example.smartsalesvisit.common.combinedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApp)
            modules(combinedModules)
        }
    }
}