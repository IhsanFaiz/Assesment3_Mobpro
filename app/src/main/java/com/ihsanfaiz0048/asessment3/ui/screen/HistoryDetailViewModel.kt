package com.ihsanfaiz0048.asessment3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import com.ihsanfaiz0048.asessment3.network.MenuApi
import com.ihsanfaiz0048.asessment3.network.OrderApi
import com.ihsanfaiz0048.asessment3.supabase.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryDetailViewModel : ViewModel() {
    suspend fun getDetailHistory(userId: String, id: Int): OrderWithMenu? {
        return withContext(Dispatchers.IO) {
            try {
                val orders = OrderApi.services.getOrderById(Constants.SUPABASE_KEY, userId, "eq.$id")
                val order = orders.firstOrNull() ?: return@withContext null
                val menus = MenuApi.services.getMenuById(Constants.SUPABASE_KEY, userId, "eq.${order.idMenu}")
                val menu = menus.firstOrNull() ?: return@withContext null
                OrderWithMenu(order, menu)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun deleteOrder(userId: String, id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                OrderApi.services.deleteOrder(Constants.SUPABASE_KEY, userId, "eq.$id")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}