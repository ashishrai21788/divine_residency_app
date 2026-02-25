package com.app.core.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
annotation class GsonTransient

object TransientExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipClass(type: Class<*>): Boolean = false
    override fun shouldSkipField(f: FieldAttributes): Boolean =
        f.getAnnotation(GsonTransient::class.java) != null
                || f.name.endsWith("\$delegate")
}

fun gsonTransient(): Gson = GsonBuilder()
    .setExclusionStrategies(TransientExclusionStrategy)
    .create()