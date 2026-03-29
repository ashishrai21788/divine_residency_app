package com.app.divine.villa.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.core.dagger.preference.AppPreferences
import com.app.core.dagger.roomdatabase.AppDatabase
import com.app.core.viewmodel.CoreViewModel
import com.app.divine.api.ApiResult
import com.app.divine.api.dto.VillaAdminUserDto
import com.app.divine.repository.VillaUsersRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class VillaUsersViewModel(
    appDatabase: AppDatabase,
    retrofit: Retrofit,
    okHttpClient: OkHttpClient,
    appPreferences: AppPreferences,
    context: Context,
    private val usersRepository: VillaUsersRepository
) : CoreViewModel(appDatabase, retrofit, okHttpClient, appPreferences, context) {

    private val _listResult = MutableLiveData<ApiResult<List<VillaAdminUserDto>>>()
    val listResult: LiveData<ApiResult<List<VillaAdminUserDto>>> = _listResult

    fun loadUsers(societyId: String?) {
        usersRepository.listUsers(societyId) { _listResult.postValue(it) }
    }
}
