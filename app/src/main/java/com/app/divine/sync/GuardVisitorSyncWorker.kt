package com.app.divine.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.app.divine.AppApplication
import com.app.divine.api.dto.VillaVisitorRegisterRequest
import com.app.divine.api.villa.VillaVisitorsApi
import com.app.divine.villa.viewmodel.VillaVisitorsViewModel
import com.google.gson.Gson

/**
 * Drains [com.app.core.dagger.coreentity.PendingGuardVisitorEntity] rows when network is available.
 */
class GuardVisitorSyncWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? AppApplication ?: return Result.failure()
        val core = app.coreComponent
        val dao = core.appDatabase().pendingGuardVisitorDao()
        val api = core.villaSocietyRetrofit().create(VillaVisitorsApi::class.java)
        val gson = Gson()
        val pending = dao.listByStatus(VillaVisitorsViewModel.SYNC_STATUS_PENDING)
        if (pending.isEmpty()) return Result.success()
        var hadFailure = false
        for (row in pending) {
            try {
                val req = gson.fromJson(row.requestJson, VillaVisitorRegisterRequest::class.java)
                val res = api.registerVisitor(req).execute()
                when {
                    res.isSuccessful -> dao.deleteById(row.id)
                    res.code() in 400..499 -> dao.deleteById(row.id)
                    else -> hadFailure = true
                }
            } catch (_: Exception) {
                hadFailure = true
            }
        }
        return if (hadFailure) Result.retry() else Result.success()
    }

    companion object {
        private const val UNIQUE_NAME = "guard_visitor_sync"

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val req = OneTimeWorkRequestBuilder<GuardVisitorSyncWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_NAME,
                ExistingWorkPolicy.KEEP,
                req
            )
        }
    }
}
