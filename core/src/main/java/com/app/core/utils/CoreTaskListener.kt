package com.app.core.utils

interface CoreTaskListener {
    fun <T> onCoreTaskResult(actionType: String?, data: T?)
}