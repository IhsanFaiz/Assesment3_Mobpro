package com.ihsanfaiz0048.asessment3.model

import com.squareup.moshi.Json

data class OrderRequest(
    @Json(name = "id_menu")
    val idMenu: Int,
    val catatan: String? = null,
    val quantity: Int,
    val totalBayar: Int,
    val tanggal: String,
    val createdAt: String? = null,
    val email: String? = null
)
