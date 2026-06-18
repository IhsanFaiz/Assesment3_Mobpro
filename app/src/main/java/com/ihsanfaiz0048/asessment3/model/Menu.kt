package com.ihsanfaiz0048.asessment3.model

import com.squareup.moshi.Json

data class Menu(
    val id: Int,
    val nama: String,
    val deskripsi: String,
    val kategori: String,
    val harga: Int,
    val gambar: String
)
