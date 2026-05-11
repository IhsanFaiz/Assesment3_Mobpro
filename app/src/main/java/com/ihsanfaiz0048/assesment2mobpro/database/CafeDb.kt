package com.ihsanfaiz0048.assesment2mobpro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ihsanfaiz0048.assesment2mobpro.R
import com.ihsanfaiz0048.assesment2mobpro.model.Menu
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Menu::class],
    version = 1,
    exportSchema = false
)
abstract class CafeDb : RoomDatabase() {

    abstract val dao: MenuDao

    companion object {

        @Volatile
        private var INSTANCE: CafeDb? = null

        fun getInstance(context: Context): CafeDb {

            synchronized(this) {

                var instance = INSTANCE

                if (instance == null) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CafeDb::class.java,
                        "cafe.db"
                    )
                        .addCallback(
                            object : RoomDatabase.Callback() {

                                override fun onCreate(
                                    db: SupportSQLiteDatabase
                                ) {
                                    super.onCreate(db)

                                    INSTANCE?.let { database ->

                                        CoroutineScope(
                                            Dispatchers.IO
                                        ).launch {

                                            database.dao.insertAll(
                                                listOf(
                                                    Menu(
                                                        nama = "Americano",
                                                        harga = 18000,
                                                        deskripsi = "Strong coffee",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.americano
                                                    ),
                                                    Menu(
                                                        nama = "Cappuccino",
                                                        harga = 18000,
                                                        deskripsi = "Strong coffee",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.cappucino
                                                    ),
                                                    Menu(
                                                        nama = "Latte",
                                                        harga = 25000,
                                                        deskripsi = "Milk coffee",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.latte
                                                    ),
                                                    Menu(
                                                        nama = "Vanilla Latte",
                                                        harga = 25000,
                                                        deskripsi = "Milk coffee",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.vanilla_latte
                                                    ),
                                                    Menu(
                                                        nama = "Caramel Latte",
                                                        harga = 25000,
                                                        deskripsi = "Milk coffee",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.caramel_latte
                                                    ),
                                                    Menu(
                                                        nama = "Croissant",
                                                        harga = 22000,
                                                        deskripsi = "Fresh pastry",
                                                        kategori = "Makanan",
                                                        gambar = R.drawable.croissant
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        )
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}