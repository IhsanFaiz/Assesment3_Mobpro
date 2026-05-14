package com.ihsanfaiz0048.assesment2mobpro.ui.screen

import androidx.lifecycle.ViewModel
import com.ihsanfaiz0048.assesment2mobpro.database.OrderDao
import com.ihsanfaiz0048.assesment2mobpro.model.OrderWithMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryDetailViewModel(private val historyDao: OrderDao): ViewModel() {
    suspend fun getDetailHistory(id: Long): OrderWithMenu{
        return withContext(Dispatchers.IO) {
            historyDao.getDetailOrderWithMenu(id)
        }
    }
}