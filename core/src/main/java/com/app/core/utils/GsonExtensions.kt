package com.app.core.utils

import com.google.gson.*
import java.lang.reflect.Type

fun Any.provideGsonWithCoreJsonString(): Gson {
    return GsonBuilder().also {
        it.registerTypeAdapter(CoreJsonString::class.java, object : JsonDeserializer<CoreJsonString?> {
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): CoreJsonString? {
                return json?.toString()?.let { time ->
                    CoreJsonString(time)
                } ?: kotlin.run { null }
            }
        })
    }.create()
}

fun provideGsonDate(): Gson {
    return GsonBuilder().also {
        it.registerTypeAdapter(DisplayableDateDMY::class.java, object : JsonDeserializer<DisplayableDateDMY?> {
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DisplayableDateDMY? {
                return json?.asJsonPrimitive?.asString?.let { time ->
                    DisplayableDateDMY(time)
                } ?: kotlin.run { null }
            }
        })
    }.create()
}