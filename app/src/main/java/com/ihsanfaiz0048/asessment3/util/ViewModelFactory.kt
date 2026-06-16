package com.ihsanfaiz0048.asessment3.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ihsanfaiz0048.asessment3.database.CafeDb
import com.ihsanfaiz0048.asessment3.ui.screen.DetailViewModel
import com.ihsanfaiz0048.asessment3.ui.screen.HistoryDetailViewModel
import com.ihsanfaiz0048.asessment3.ui.screen.HistoryViewModel
import com.ihsanfaiz0048.asessment3.ui.screen.MainViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val daoMenu = CafeDb.Companion.getInstance(context).menuDao
        val daoOrder = CafeDb.Companion.getInstance(context).orderDao
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(daoMenu) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(daoMenu, daoOrder) as T
        } else if (modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(daoOrder) as T
        } else if (modelClass.isAssignableFrom(HistoryDetailViewModel::class.java)){
            return HistoryDetailViewModel(daoOrder) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}