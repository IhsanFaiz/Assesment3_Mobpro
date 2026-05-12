package com.ihsanfaiz0048.assesment2mobpro.navigation


const val KEY_ID_MENU = "idMenu"

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object HistoryScreen: Screen("historyScreen")
    data object DetailMenu: Screen("detailScreen/{$KEY_ID_MENU}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}