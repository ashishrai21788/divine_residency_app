package com.app.core.extensions

import android.content.Intent
import android.os.Bundle


fun Intent.putExtraData(extraData: Bundle?) {
    this.putExtra("extraData", extraData)
}



fun Intent.hasExtraData(): Boolean {
    return this.hasExtra("extraData") && this.getBundleExtra("extraData") != null
}

fun Intent.getExtraData(): Bundle? {
    return this.getBundleExtra("extraData")
}



fun Intent.putTwilioNotificationData(notificationDataBundle: Bundle?) {
    this.putExtra("twilio_notification_data", notificationDataBundle)
}

fun Intent.getTwilioNotificationData(): Bundle? {
    return this.getParcelableExtra("twilio_notification_data") as? Bundle
}

