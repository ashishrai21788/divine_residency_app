package com.app.core.utils

import android.location.Location

interface CoreLocationSettingsListener {
    fun noResolutionFound()

    // if you using to check location settings only then must override this function
    fun readyToFetchLocation() {}

    /*************************** These function will never called if you opted to check location settings*************************/
    // if you want to fetch device's current location
    fun onCurrentLocationReceived(currentLocation: Location) {}
    fun permissionDenied(deniedForever: Boolean = false) {}
}