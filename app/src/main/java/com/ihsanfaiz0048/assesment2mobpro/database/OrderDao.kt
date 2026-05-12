package com.ihsanfaiz0048.assesment2mobpro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ihsanfaiz0048.assesment2mobpro.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order)

    @Update
    suspend fun update(order: Order)

    @Query("SELECT * FROM orders o JOIN menu m on m.id = o.idMenu")
    fun getOrderWithMenu(): Flow<List<Order>>

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteById(id: Long)
}