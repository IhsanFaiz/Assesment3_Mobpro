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
                                                        deskripsi = "Espresso dengan tambahan air panas.",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.americano
                                                    ),
                                                    Menu(
                                                        nama = "Cappuccino",
                                                        harga = 18000,
                                                        deskripsi = "Perpaduan espresso, susu, dan foam lembut.",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.cappucino
                                                    ),
                                                    Menu(
                                                        nama = "Latte",
                                                        harga = 25000,
                                                        deskripsi = "Kopi susu creamy dengan rasa smooth.",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.latte
                                                    ),
                                                    Menu(
                                                        nama = "Vanilla Latte",
                                                        harga = 25000,
                                                        deskripsi = "Kopi susu creamy dengan syrup vanilla.",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.vanilla_latte
                                                    ),
                                                    Menu(
                                                        nama = "Caramel Latte",
                                                        harga = 25000,
                                                        deskripsi = "Kopi susu creamy dengan syrup caramel.",
                                                        kategori = "Minuman",
                                                        gambar = R.drawable.caramel_latte
                                                    ),
                                                    Menu(
                                                        nama = "Croissant",
                                                        harga = 22000,
                                                        deskripsi = "Pastry buttery dengan tekstur renyah.",
                                                        kategori = "Makanan",
                                                        gambar = R.drawable.croissant
                                                    ),
                                                    Menu(
                                                        nama = "Espresso",
                                                        deskripsi = "Kopi hitam pekat dengan aroma strong.",
                                                        kategori = "Minuman",
                                                        harga = 18000,
                                                        gambar = R.drawable.espresso
                                                    ),
                                                    Menu(
                                                        nama = "Mocha",
                                                        deskripsi = "Kopi dengan campuran coklat premium.",
                                                        kategori = "Minuman",
                                                        harga = 30000,
                                                        gambar = R.drawable.mocha
                                                    ),
                                                    Menu(
                                                        nama = "Cheesecake",
                                                        deskripsi = "Cake lembut dengan topping cream cheese.",
                                                        kategori = "Makanan",
                                                        harga = 35000,
                                                        gambar = R.drawable.cheesecake
                                                    ),
                                                    Menu(
                                                        nama = "French Fries",
                                                        deskripsi = "Kentang goreng crispy dengan saus spesial.",
                                                        kategori = "Makanan",
                                                        harga = 24000,
                                                        gambar = R.drawable.french_fries
                                                    ),
                                                    Menu(
                                                        nama = "Club Sandwich",
                                                        deskripsi = "Sandwich isi ayam, sayur, dan keju.",
                                                        kategori = "Makanan",
                                                        harga = 32000,
                                                        gambar = R.drawable.club_sandwich
                                                    ),
                                                    Menu(
                                                        nama = "Donut",
                                                        deskripsi = "Donat empuk dengan topping manis.",
                                                        kategori = "Makanan",
                                                        harga = 18000,
                                                        gambar = R.drawable.donut
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