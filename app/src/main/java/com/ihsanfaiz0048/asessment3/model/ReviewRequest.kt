package com.ihsanfaiz0048.asessment3.model

import com.squareup.moshi.Json

data class ReviewRequest(
    val review: String,
    val star: Int,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "menu_id")
    val menuId: Int,
    val email: String,
    @Json(name = "order_id")
    val orderId: Int
)
