package com.ihsanfaiz0048.asessment3.ui.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.asessment3.model.Menu
import com.ihsanfaiz0048.asessment3.model.Order
import com.ihsanfaiz0048.asessment3.model.OrderRequest
import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import com.ihsanfaiz0048.asessment3.model.Review
import com.ihsanfaiz0048.asessment3.network.ApiStatus
import com.ihsanfaiz0048.asessment3.network.MenuApi
import com.ihsanfaiz0048.asessment3.network.OrderApi
import com.ihsanfaiz0048.asessment3.network.ReviewApi
import com.ihsanfaiz0048.asessment3.supabase.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class DetailViewModel : ViewModel() {

    fun insertOrder(userId: String, idMenu: Int, catatan: String?, quantity: Int, totalBayar: Int){
        val order = OrderRequest(
            idMenu = idMenu,
            catatan = catatan,
            quantity = quantity,
            totalBayar = totalBayar,
            tanggal = Instant.now().toString(),
            createdAt = Instant.now().toString(),
            email = userId
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =
                    OrderApi.services.createOrder(
                        Constants.SUPABASE_KEY,
                        userId,
                        order
                    )

                Log.d("ORDER", response.toString() + "error fetch")
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ORDER", e.message ?: "Unknown error")
            }
        }
    }

    fun updateOrder(userId: String, idOrder: Int, idMenu: Int, catatan: String?, quantity: Int, totalBayar: Int){
        val order = Order(
            id = idOrder,
            idMenu = idMenu,
            catatan = catatan,
            quantity = quantity,
            totalBayar = totalBayar,
            tanggal = Instant.now().toString()
        )

        viewModelScope.launch(Dispatchers.IO) {
            try {
                OrderApi.services.updateOrder(Constants.SUPABASE_KEY, userId, "eq.$idOrder", order)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getMenu(id: Int, userId: String): Menu? {
        return try {
            val list = MenuApi.services.getMenuById(Constants.SUPABASE_KEY, userId, "eq.$id")
            list.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getOrderWithMenuEdit(userId: String, idOrder: Int): OrderWithMenu? {
        return try {
            val orders = OrderApi.services.getOrderById(Constants.SUPABASE_KEY, userId, "eq.$idOrder")
            val order = orders.firstOrNull() ?: return null
            val menuList = MenuApi.services.getMenuById(Constants.SUPABASE_KEY, userId, "eq.${order.idMenu}")
            val menu = menuList.firstOrNull() ?: return null
            OrderWithMenu(order, menu)
        } catch (e: Exception) {
            null
        }
    }

    private val _reviewState = MutableStateFlow<List<Review>>(emptyList())
    val reviewState: StateFlow<List<Review>> = _reviewState

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _status = MutableStateFlow(ApiStatus.LOADING)
    val status: StateFlow<ApiStatus> = _status


    fun loadReviews(menuId: Int, userId: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val response = ReviewApi.services.getReview(
                    Constants.SUPABASE_KEY,
                    userId,
                    "eq.$menuId"
                )
                _reviewState.value = response
                _status.value = ApiStatus.SUCCESS
            }catch (e: Exception){
                _errorMessage.value = e.message
                _status.value = ApiStatus.FAILED
                Log.e("MainViewModel", "Error fetching data: ${e.message}", e)
            }
        }
    }

}