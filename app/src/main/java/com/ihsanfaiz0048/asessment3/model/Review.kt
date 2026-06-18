package com.ihsanfaiz0048.asessment3.model

import com.squareup.moshi.Json

data class Review(
    val id: Int,
    val review: String,
    val star: Int,
    @Json(name = "image_url")
    val imageUrl: String,
    @Json(name = "menu_id")
    val menuId: Int,
    val email: String
)
