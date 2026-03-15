package com.example.smartsalesvisit.common


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import org.koin.java.KoinJavaComponent.get

class SyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val syncManager: SyncManager = get(SyncManager::class.java)

    override suspend fun doWork(): Result {

        return try {

            println("WORKMANAGER STARTED")

            syncManager.syncVisits()

            println("WORKMANAGER FINISHED")

            Result.success()

        } catch (e: Exception) {

            println("WORKMANAGER FAILED: ${e.message}")

            Result.retry()
        }

    }
}