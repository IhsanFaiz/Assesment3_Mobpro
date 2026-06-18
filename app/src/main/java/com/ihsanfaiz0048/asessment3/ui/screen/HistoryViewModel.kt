package com.ihsanfaiz0048.asessment3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import com.ihsanfaiz0048.asessment3.network.ApiStatus
import com.ihsanfaiz0048.asessment3.network.MenuApi
import com.ihsanfaiz0048.asessment3.network.OrderApi
import com.ihsanfaiz0048.asessment3.supabase.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private val _dataHistory = MutableStateFlow<List<OrderWithMenu>>(emptyList())
    val dataHistory: StateFlow<List<OrderWithMenu>> = _dataHistory

    private val _status = MutableStateFlow(ApiStatus.LOADING)

    val status: StateFlow<ApiStatus> = _status

    fun loadData(userId: String) {
        if (userId.isEmpty()) return
        
        viewModelScope.launch(Dispatchers.IO) {
            _status.value = ApiStatus.LOADING
            try {
                val orders = OrderApi.services.getOrders(Constants.SUPABASE_KEY, userId, "eq.${userId}")
                val result = orders.mapNotNull { order ->
                    val menus = MenuApi.services.getMenuById(Constants.SUPABASE_KEY, userId, "eq.${order.idMenu}")
                    val menu = menus.firstOrNull()
                    if (menu != null) OrderWithMenu(order, menu) else null
                }
                _dataHistory.value = result
                _status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                e.printStackTrace()
                _status.value = ApiStatus.FAILED
            }
        }
    }
}