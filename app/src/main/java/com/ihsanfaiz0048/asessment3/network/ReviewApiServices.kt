package com.ihsanfaiz0048.asessment3.network

import com.ihsanfaiz0048.asessment3.model.Review
import com.ihsanfaiz0048.asessment3.supabase.Constants
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
    @GET("review")
    suspend fun getReview(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("menuId") menuId: String,
        @Query("select") select: String = "*"
    ): List<Review>

    @POST("review")
    suspend fun insertReview(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Body review: Review
    )
}

object ReviewApi {
    val services: ReviewApiServices by lazy {
        retrofit.create(ReviewApiServices::class.java)
    }
}