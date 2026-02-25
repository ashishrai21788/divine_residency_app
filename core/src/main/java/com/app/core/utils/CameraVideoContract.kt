package com.app.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract


class CameraVideoContract : ActivityResultContract<Uri, Uri?>() {
    var uri: Uri? = null
    override fun createIntent(context: Context, input: Uri): Intent {
        uri = input
        return Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun getSynchronousResult(context: Context, input: Uri): SynchronousResult<Uri?>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && intent == null) {
            if (uri != null) {
                val intentNullCapture = Intent()
                intentNullCapture.data = uri
                intentNullCapture.data
            } else {
                null
            }
        } else if (intent == null || resultCode != Activity.RESULT_OK) {
            null
        } else {
            intent.data
        }
    }
}
