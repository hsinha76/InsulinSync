package com.hsdroid.insulinsync

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application(){

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        WorkManager.initialize(applicationContext, getWorkManagerConfiguration())
    }

    fun getWorkManagerConfiguration() = Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
}