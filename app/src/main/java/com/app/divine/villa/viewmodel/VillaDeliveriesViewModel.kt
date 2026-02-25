package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaDeliveryCreateRequest
import com.app.divine.api.dto.VillaDeliveryDto
import com.app.divine.repository.VillaDeliveriesRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaDeliveriesViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val deliveriesRepository: VillaDeliveriesRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _createResult = MutableLiveData<ApiResult<VillaDeliveryDto>>()
    val createResult: LiveData<ApiResult<VillaDeliveryDto>> = _createResult

    private val _markReceivedResult = MutableLiveData<ApiResult<VillaDeliveryDto>>()
    val markReceivedResult: LiveData<ApiResult<VillaDeliveryDto>> = _markReceivedResult

    private val _listResult = MutableLiveData<ApiResult<List<VillaDeliveryDto>>>()
    val listResult: LiveData<ApiResult<List<VillaDeliveryDto>>> = _listResult

    fun createDelivery(request: VillaDeliveryCreateRequest) {
        deliveriesRepository.createDelivery(request) { _createResult.postValue(it) }
    }

    fun markReceived(deliveryId: String) {
        deliveriesRepository.markReceived(deliveryId) { _markReceivedResult.postValue(it) }
    }

    fun loadDeliveries(floorId: String? = null, societyId: String? = null) {
        deliveriesRepository.getDeliveries(floorId, societyId) { _listResult.postValue(it) }
    }
}
