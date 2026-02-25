package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaBillDto
import com.app.divine.api.dto.VillaPaymentDto
import com.app.divine.repository.VillaBillingRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaBillingViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val billingRepository: VillaBillingRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _myBillsResult = MutableLiveData<ApiResult<List<VillaBillDto>>>()
    val myBillsResult: LiveData<ApiResult<List<VillaBillDto>>> = _myBillsResult

    private val _pendingBillsResult = MutableLiveData<ApiResult<List<VillaBillDto>>>()
    val pendingBillsResult: LiveData<ApiResult<List<VillaBillDto>>> = _pendingBillsResult

    private val _myPaymentsResult = MutableLiveData<ApiResult<List<VillaPaymentDto>>>()
    val myPaymentsResult: LiveData<ApiResult<List<VillaPaymentDto>>> = _myPaymentsResult

    fun loadMyBills(societyId: String) {
        billingRepository.getMyBills(societyId) { _myBillsResult.postValue(it) }
    }

    fun loadMyPendingBills(societyId: String) {
        billingRepository.getMyPendingBills(societyId) { _pendingBillsResult.postValue(it) }
    }

    fun loadMyPayments(societyId: String) {
        billingRepository.getMyPayments(societyId) { _myPaymentsResult.postValue(it) }
    }
}
