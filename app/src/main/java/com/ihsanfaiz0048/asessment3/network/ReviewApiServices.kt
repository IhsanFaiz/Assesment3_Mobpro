package com.ihsanfaiz0048.asessment3.network

import com.ihsanfaiz0048.asessment3.model.Review
import com.ihsanfaiz0048.asessment3.model.ReviewRequest
import com.ihsanfaiz0048.asessment3.supabase.Constants
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = Constants.SUPABASE_URL


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ReviewApiServices {
    @GET("rest/v1/review")
    suspend fun getReview(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("menu_id") menuId: String,
        @Query("select") select: String = "*"
    ): List<Review>

    @POST("rest/v1/review")
    suspend fun insertReview(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Body review: ReviewRequest
    )

    @POST("storage/v1/object/{bucket}/{image_name}")
    suspend fun uploadImage(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Header("Content-Type") contentType: String, // <-- TAMBAHKAN INI
        @Path("bucket") bucket: String,
        @Path("image_name") imageName: String,
        @Body file: RequestBody // <-- KEMBALI KE BERSIHNYA REQUESTBODY
    ): ResponseBody

    @GET("rest/v1/review")
    suspend fun checkReviewByOrder(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("order_id") orderIdFilter: String
    ): List<Review>
}

object ReviewApi {
    val services: ReviewApiServices by lazy {
        retrofit.create(ReviewApiServices::class.java)
    }
}