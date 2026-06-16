package com.ihsanfaiz0048.asessment3.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihsanfaiz0048.asessment3.database.OrderDao
import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryDetailViewModel(private val historyDao: OrderDao): ViewModel() {
    suspend fun getDetailHistory(id: Long): OrderWithMenu? {
        return withContext(Dispatchers.IO) {
            historyDao.getDetailOrderWithMenu(id)
        }
    }

    fun deleteOrder(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            historyDao.deleteById(id)
        }
    }
}