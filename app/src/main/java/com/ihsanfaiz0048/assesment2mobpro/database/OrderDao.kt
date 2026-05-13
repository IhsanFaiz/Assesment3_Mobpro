package com.ihsanfaiz0048.assesment2mobpro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ihsanfaiz0048.assesment2mobpro.model.Order
import com.ihsanfaiz0048.assesment2mobpro.model.OrderWithMenu
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Query("SELECT o.*, m.id AS 'menu_id', m.nama AS 'menu_nama', m.kategori AS 'menu_kategori', m.deskripsi AS 'menu_deskripsi', m.harga AS 'menu_harga', m.gambar AS 'menu_gambar' FROM orders o JOIN menu m on m.id = o.idMenu ORDER BY o.tanggal DESC")
    fun getOrderWithMenu(): Flow<List<OrderWithMenu>>

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteById(id: Long)
}