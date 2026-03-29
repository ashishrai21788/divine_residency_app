package com.app.divine.villa

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.divine.AppApplication
import com.app.divine.api.villa.VillaBillingApi
import com.app.divine.api.villa.VillaComplaintsApi
import com.app.divine.api.villa.VillaDeliveriesApi
import com.app.divine.api.villa.VillaGuardAttendanceApi
import com.app.divine.api.villa.VillaNoticesApi
import com.app.divine.api.villa.VillaSOSApi
import com.app.divine.api.villa.VillaUsersApi
import com.app.divine.api.villa.VillaVisitorsApi
import com.app.divine.repository.VillaBillingRepository
import com.app.divine.repository.VillaComplaintsRepository
import com.app.divine.repository.VillaDeliveriesRepository
import com.app.divine.repository.VillaGuardAttendanceRepository
import com.app.divine.repository.VillaNoticesRepository
import com.app.divine.repository.VillaSOSRepository
import com.app.divine.repository.VillaUsersRepository
import com.app.divine.repository.VillaVisitorsRepository
import com.app.divine.villa.viewmodel.VillaBillingViewModel
import com.app.divine.villa.viewmodel.VillaComplaintsViewModel
import com.app.divine.villa.viewmodel.VillaDeliveriesViewModel
import com.app.divine.villa.viewmodel.VillaGuardAttendanceViewModel
import com.app.divine.villa.viewmodel.VillaNoticesViewModel
import com.app.divine.villa.viewmodel.VillaSOSViewModel
import com.app.divine.villa.viewmodel.VillaUsersViewModel
import com.app.divine.villa.viewmodel.VillaVisitorsViewModel
import com.google.gson.Gson

/**
 * Creates Villa ViewModels using CoreComponent (Villa Retrofit + repositories).
 * Use from fragments: ViewModelProvider(this, VillaViewModelFactory(requireContext()))[VillaXxxViewModel::class.java]
 */
class VillaViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    private val app get() = context.applicationContext as AppApplication
    private val core get() = app.coreComponent

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appDatabase = core.appDatabase()
        val retrofit = core.retrofit()
        val okHttpClient = core.okHttpClient()
        val appPreferences = core.appPreferences()
        val ctx = core.context()

        return when (modelClass) {
            VillaVisitorsViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaVisitorsApi::class.java)
                VillaVisitorsViewModel(
                    appDatabase,
                    retrofit,
                    okHttpClient,
                    appPreferences,
                    ctx,
                    VillaVisitorsRepository(api),
                    core.appDatabase().pendingGuardVisitorDao(),
                    Gson(),
                    context.applicationContext
                ) as T
            }
            VillaBillingViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaBillingApi::class.java)
                VillaBillingViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaBillingRepository(api)) as T
            }
            VillaComplaintsViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaComplaintsApi::class.java)
                VillaComplaintsViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaComplaintsRepository(api)) as T
            }
            VillaDeliveriesViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaDeliveriesApi::class.java)
                VillaDeliveriesViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaDeliveriesRepository(api)) as T
            }
            VillaSOSViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaSOSApi::class.java)
                VillaSOSViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaSOSRepository(api)) as T
            }
            VillaGuardAttendanceViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaGuardAttendanceApi::class.java)
                VillaGuardAttendanceViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaGuardAttendanceRepository(api)) as T
            }
            VillaNoticesViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaNoticesApi::class.java)
                VillaNoticesViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaNoticesRepository(api)) as T
            }
            VillaUsersViewModel::class.java -> {
                val api = core.villaSocietyRetrofit().create(VillaUsersApi::class.java)
                VillaUsersViewModel(appDatabase, retrofit, okHttpClient, appPreferences, ctx, VillaUsersRepository(api)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}
