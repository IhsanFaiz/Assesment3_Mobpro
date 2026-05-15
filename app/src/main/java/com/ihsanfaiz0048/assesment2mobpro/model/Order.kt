package com.ihsanfaiz0048.assesment2mobpro.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = Menu::class,
            parentColumns = ["id"],
            childColumns = ["idMenu"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("idMenu")]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val idMenu: Long,
    val catatan: String? = null,
    val quantity: Int,
    val totalBayar: Int,
    val tanggal: String,
    val createdAt: Long = System.currentTimeMillis()
)
