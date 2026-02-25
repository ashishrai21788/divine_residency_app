package com.app.core.utils

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import com.app.core.dagger.component.CoreComponent

@Keep
interface CoreComponentProvider {
    /**
     * Returns the CoreComponent / DI root.
     */
    fun provideCoreComponent(): CoreComponent


//    fun provideLoggedUserData(): CoreUserInfo?
//
//    fun provideLoggedUserLiveData(): LiveData<CoreUserInfo?>
//
//    fun forceUpdateUserRecord(coreUserInfo: CoreUserInfo)

    fun forceLoggedOut()

    fun logFacebookEvent(name: String?)

    fun removeChatSupport()

    fun getChatSupportVisibilityData(): LiveData<Boolean>

//    fun updatePushToken(
//        coreUserInfo: CoreUserInfo? = null,
//        shouldFetchTokenOnDemand: Boolean = true
//    )



    fun logFirebaseEvent(
        pageIdentifier: String,
        eventName: String,
        eventParams: HashMap<String, String>,
        keepActualKey: Boolean = false
    )


    /*fun sendApiLogsToServerSync(jsonData: String): String*/

    fun fetchFCMToken(
        firebaseSenderId: String? = "",
        callBack: ((String) -> Unit)? = null
    )
}