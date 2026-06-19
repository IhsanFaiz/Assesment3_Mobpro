package com.ihsanfaiz0048.asessment3.network

import com.ihsanfaiz0048.asessment3.model.Order
import com.ihsanfaiz0048.asessment3.model.OrderRequest
import com.ihsanfaiz0048.asessment3.supabase.Constants
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = Constants.SUPABASE_URL

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface OrderApiServices {

    @GET("rest/v1/order")
    suspend fun getOrders(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("email") email: String,
        @Query("select") select: String = "*"
    ): List<Order>

    @GET("rest/v1/order")
    suspend fun getOrderById(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Query("select") select: String = "*"
    ): List<Order>

    @POST("rest/v1/order")
    suspend fun createOrder(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Body order: OrderRequest
    )

    @PATCH("rest/v1/order")
    suspend fun updateOrder(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Body order: Order
    )

    @DELETE("rest/v1/order")
    suspend fun deleteOrder(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("id") id: String
    )
}

object OrderApi {
    val services: OrderApiServices by lazy {
        retrofit.create(OrderApiServices::class.java)
    }
}
