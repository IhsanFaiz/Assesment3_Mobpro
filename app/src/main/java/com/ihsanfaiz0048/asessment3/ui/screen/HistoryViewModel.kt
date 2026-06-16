package com.ihsanfaiz0048.asessment3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.asessment3.database.OrderDao
import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel(historyDao: OrderDao): ViewModel() {
    val dataHistory: StateFlow<List<OrderWithMenu>> = historyDao.getOrderWithMenu().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
}