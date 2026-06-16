package com.ihsanfaiz0048.asessment3.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val deskripsi: String,
    val kategori: String,
    val harga: Int,
    val gambar: Int
)
