package com.ihsanfaiz0048.assesment2mobpro.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.assesment2mobpro.database.OrderDao
import com.ihsanfaiz0048.assesment2mobpro.model.Order
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(private val historyDao: OrderDao): ViewModel() {
    val dataHistory: StateFlow<List<Order>> = historyDao.getOrderWithMenu().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}