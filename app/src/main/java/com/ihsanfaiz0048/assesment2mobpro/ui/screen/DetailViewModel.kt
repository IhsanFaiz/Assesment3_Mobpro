package com.ihsanfaiz0048.assesment2mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.assesment2mobpro.database.MenuDao
import com.ihsanfaiz0048.assesment2mobpro.model.Menu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: MenuDao) : ViewModel() {
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(nama: String, deskripsi: String, kategori: String, harga: Int, gambar: Int){
        val menu = Menu(
            nama = nama,
            deskripsi = deskripsi,
            kategori = kategori,
            harga = harga,
            gambar = gambar
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(menu)
        }


    }

    suspend fun getMenu(id: Long): Menu? {
        return dao.getMenuById(id)
    }

    fun update(nama: String, deskripsi: String, kategori: String, harga: Int, gambar: Int){
        val menu = Menu(
            nama = nama,
            deskripsi = deskripsi,
            kategori = kategori,
            harga = harga,
            gambar = gambar
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.update(menu)
        }
    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}