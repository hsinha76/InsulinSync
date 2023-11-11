package com.hsdroid.insulinsync.utils

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.hsdroid.insulinsync.data.local.dao.StoreDao
import com.hsdroid.insulinsync.models.Insulin
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope

@HiltWorker
class DbWorker @AssistedInject constructor(
    @Assisted context: Context, @Assisted workerParameters: WorkerParameters, val dao: StoreDao
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = coroutineScope {
        Log.d("harish", "Work manager Triggered :)")

        val currentTime = System.currentTimeMillis()
        val futureTime = currentTime + 2 * 60 * 60 * 1000; // 2 hours in milliseconds
        val result = dao.getTimestamp(currentTime, futureTime)

        result.forEach {
            Helper.setAlarm(applicationContext, it)
        }
        Result.success()
    }
}