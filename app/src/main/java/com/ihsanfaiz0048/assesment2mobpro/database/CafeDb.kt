package com.ihsanfaiz0048.assesment2mobpro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ihsanfaiz0048.assesment2mobpro.model.Menu

@Database(entities = [Menu::class], version = 1, exportSchema = false)
abstract class CafeDb : RoomDatabase() {
    abstract val dao: MenuDao

    companion object {
        @Volatile
        private  var INSTANCE: CafeDb? = null
        fun getInstance(context: Context): CafeDb {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CafeDb::class.java,
                        "cafe.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}