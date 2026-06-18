package com.ihsanfaiz0048.asessment3.network

import com.ihsanfaiz0048.asessment3.model.Menu
import com.ihsanfaiz0048.asessment3.supabase.Constants
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

private const val BASE_URL = Constants.SUPABASE_URL

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface MenuApiServices {

    @GET("menu")
    suspend fun getMenu(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("select") select: String = "*"
    ): List<Menu>

    @GET("menu")
    suspend fun getMenuById(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Query("id") id: String,
        @Query("select") select: String = "*"
    ): List<Menu>
    
}

object MenuApi{
    val services: MenuApiServices by lazy {
        retrofit.create(MenuApiServices::class.java)
    }
}

enum class ApiStatus {LOADING, SUCCESS, FAILED}
