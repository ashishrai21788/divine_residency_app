package com.app.core.rest

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

public interface CoreCommonService {


    /* foodcourt apis start*/
    @GET
    @Headers(value = ["Content-Type:application/json"])
    fun coreLocationSearch(
        @Url url: String,
        @Query("key") key: String,
        @Query("input") search: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreLocationFromAddress(
        @Url url: String,
        @Query("address") query: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreLocationFromAddressReference(
        @Url url: String,
        @Query("reference") reference: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreLocationLatLongFromAddress(
        @Url url: String,
        @Query("placeid") query: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreAddressFromLatLang(
        @Url url: String,
        @Query("latlng") query: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreAddressFromPlusCode(
        @Url url: String,
        @Query("address") query: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreDistanceBetweenLocation(
        @Url url: String,
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") key: String,
        @Query(
            "units"
        ) unit: String = "miles",
        @Query("mode") mode: String = "DRIVING"
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreDirection(
        @Url url: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreDirection(
        @Url url: String,
        @Query("origin") origin: String,
        @Query("source") source: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Observable<JsonObject>


    /* foodcourt apis end*/


    /* directory apis start*/
    @POST
    fun addCouponRequest(@Url url: String, @Body requestBody: RequestBody): Observable<JsonObject>

    @GET
    @Headers("Content-Type:application/json")
    fun coreDirectionWithMode(
        @Url url: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") key: String
    ): Observable<JsonObject>

    /* directory apis end*/


    @GET
    @Headers("Content-Type:application/json")
    fun getLocationFromAddress(
        @Url url: String,
        @Query("address") query: String,
        @Query("key") key: String
    ): Observable<JsonObject>


    @GET
    @Headers("Content-Type:application/json")
    fun getDistanceBetweenPoints(
        @Url url: String,
        @Query("address") query: String,
        @Query("key") key: String
    ): Observable<JsonObject>


    @Multipart
    @POST
    fun uploadCustomisationWithImage(
        @Url url: String, @Part("cartId") cartId: RequestBody,
        @Part("appId") appId: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("pageId") pageId: RequestBody?,
        @Part("orderId") orderId: RequestBody?,
        @Part("text") text: RequestBody?,
        @Part("userInteractionText") userInteractionText: RequestBody?

    ): Observable<JsonObject>

    @Multipart
    @POST
    fun uploadCustomisationWithText(
        @Url url: String, @Part("cartId") cartId: RequestBody,
        @Part("appId") appId: RequestBody,
        @Part("pageId") pageId: RequestBody?,
        @Part("orderId") orderId: RequestBody?,
        @Part("text") text: RequestBody?,
        @Part("userInteractionText") userInteractionText: RequestBody?
    ): Observable<JsonObject>

    @POST
    fun reportChatRoomImage(@Url url: String, @Body requestBody: JsonObject): Observable<JsonObject>


    @POST
    @Headers("Content-Type:application/json")
    fun ottApi(
        @Url url: String,
        @Body requestBody: JsonObject
    ): Call<JsonObject>

    @POST
    @Headers("Content-Type:application/json")
    fun ottVideoToken(
        @Url url: String,
        @Body requestBody: JsonObject
    ): Call<JsonObject>
}


object CoreCommonBaseUrl {
    val LOCATION_SEARCH_BASE_URL = "https://maps.googleapis.com/maps/api/place/autocomplete/json"
    val PLACE_DETAIL_BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json"
    val GEO_CODER_BASE_URL = "https://maps.googleapis.com/maps/api/geocode/json"
    val PLUS_BASE_URL = "https://plus.codes/api"
    val DISTANCE_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json"
    val DIRECTION_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json"

    /* directory apis start*/
    val AR_BASE_URL = "https://maps.googleapis.com/"
    /* directory apis end*/


}