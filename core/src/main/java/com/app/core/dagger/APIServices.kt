package com.app.core.dagger

import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Url


interface APIServices {


    @GET
    suspend fun makeHttpSingleDataRequest( @Url url: String): String
    @GET
    suspend fun makeHttpGetDownloadRequest( @Url url: String): ResponseBody

    @GET
    fun makeHttpGetRequest(@Url url: String): Call<Any>

    @GET
    fun makeHttpGetRequestYouTubeChannel(@Url url: String): Call<JSONObject>

    @GET
    fun makeHttpGetFacebookRequestString(@Url url: String): Call<JSONObject>

    @GET
    fun makeHttpGetRequestString(@Url url: String): Call<String>

    @GET
    fun makeHttpGetXmlRequest(@Url url: String): Call<Any>

    @POST
    fun makeHttpPostRequest(@Url url: String, @Body body: JsonObject?): Call<JsonObject>


    @POST
    fun makeHttpPostRequest(@Url url: String, @Body body: HashMap<String, String>): Call<Any>

    @POST
    fun makeHttpJsonPostRequest(@Url url: String, @Body body: HashMap<String, String>): Call<JsonObject>

    @POST
    fun makeHttpJsonPostRequest(@Url url: String, @HeaderMap headers: HashMap<String, String>, @Body body: JsonObject): Call<JsonObject>

    @POST
    fun makeMultipartJsonResRequest(@Url url: String, @Body requestBody: RequestBody): Call<JsonObject>

    @POST
    fun uploadImage(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>

    @POST
    fun getGoogleSheetToken(@Url url: String, @Body body: JsonObject): Observable<JSONObject>

    @GET
    fun getAppsheetData(@Url url: String, @HeaderMap headers: HashMap<String, String>): Call<JsonObject>

    @PUT
    fun updateAppsheetData(@Url url: String, @HeaderMap headers: HashMap<String, String>, @Body body: JsonObject): Call<JsonObject>

    @POST
    fun newEntryAppsheetData(@Url url: String, @HeaderMap headers: HashMap<String, String>, @Body body: JsonObject): Call<JsonObject>

    @POST
    abstract fun uploadAppsheetFiles(@Url url: String, @Body finalRequestBody: RequestBody): Observable<JsonObject>

    @FormUrlEncoded
    @POST
    fun getOneDriveHttpPostRequest(
        @Url url: String,
        @Field("client_id") clientId: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("client_secret") clientSecret: String,
        @Field("refresh_token") refreshToken: String,
        @Field("grant_type") grantType: String
    ): Call<Any>

    @FormUrlEncoded
    @POST
    fun getDocumentDriveHttpPostRequest(
        @Url url: String, @Field("grant_type") grantType: String, @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String, @Field("client_secret") clientSecret: String
    ): Call<Any>

    @POST
    fun commentForumAnswer(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>

    @POST
    fun uploadGalleryImage(@Url url: String, @Body finalRequestBody: RequestBody): Call<Any>

    @POST
    fun registerUser(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>

    @POST
    fun updateUser(@Url url: String, @Body finalRequestBody: RequestBody): Call<JsonObject>

    @POST
    fun postCustomForm(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>

    @POST
    fun postAppointmentForm(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>

    @POST
    fun postNestedForm(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>


    @POST
    fun retrieveAWSCredentials(@Url url: String, @Body body: JsonObject): Call<JSONObject>

    @POST
    fun recipeAdd(@Url url: String, @Body finalRequestBody: RequestBody): Call<Any>

    @POST
    fun postOrderForm(@Url url: String, @Body finalRequestBody: RequestBody): Call<JSONObject>

    @POST
    fun postHIPAAForm(
        @Url url: String,
        @Body finalRequestBody: RequestBody
    ): Call<JSONObject>

    @POST
    fun retrieveRazorPayOrderId(@Url url: String, @Body request: JSONObject): Call<JSONObject>

    @GET
    @Headers("Content-Type:application/json")
    fun getClientIpInfo(@Url url: String): Call<JsonObject>
}