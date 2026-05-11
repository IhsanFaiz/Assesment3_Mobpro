package com.ihsanfaiz0048.assesment2mobpro.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ihsanfaiz0048.assesment2mobpro.model.Menu
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {
    @Insert
    suspend fun insert(menu: Menu)

    @Update
    suspend fun update(menu: Menu)

    @Query("SELECT * FROM menu ORDER BY nama ASC")
    fun getMenu(): Flow<List<Menu>>

    @Query("SELECT * FROM menu WHERE id = :id")
    suspend fun getMenuById(id: Long): Menu?

    @Query("DELETE FROM menu WHERE id = :id")
    suspend fun deleteById(id: Long)
}
