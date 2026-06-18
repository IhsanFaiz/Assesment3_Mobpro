package com.ihsanfaiz0048.asessment3.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.asessment3.model.Menu
import com.ihsanfaiz0048.asessment3.network.ApiStatus
import com.ihsanfaiz0048.asessment3.network.MenuApi
import com.ihsanfaiz0048.asessment3.supabase.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _dataMenu = MutableStateFlow<List<Menu>>(emptyList())
    val data: StateFlow<List<Menu>> = _dataMenu

    val dataMakanan: StateFlow<List<Menu>> = _dataMenu.map { list ->
        list.filter { it.kategori.equals("Makanan", ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dataMinuman: StateFlow<List<Menu>> = _dataMenu.map { list ->
        list.filter { it.kategori.equals("Minuman", ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _status = MutableStateFlow(ApiStatus.LOADING)
    val status: StateFlow<ApiStatus> = _status

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = ApiStatus.LOADING
            try {
                // Supabase requires both 'apikey' and 'Authorization' headers.
                // The select query parameter is also usually required (defaulted to "*" in service).
                val response = MenuApi.services.getMenu(
                    apiKey = Constants.SUPABASE_KEY,
                    authorization = "Bearer ${Constants.SUPABASE_KEY}"
                )
                _dataMenu.value = response
                _status.value = ApiStatus.SUCCESS
                Log.d("MainViewModel", "Data fetched successfully: ${response.size} items")
                Log.d("MainViewModel", "Response content: $response")
            } catch (e: Exception) {
                _errorMessage.value = e.message
                _status.value = ApiStatus.FAILED
                Log.e("MainViewModel", "Error fetching data: ${e.message}", e)
            }
        }
    }
}
