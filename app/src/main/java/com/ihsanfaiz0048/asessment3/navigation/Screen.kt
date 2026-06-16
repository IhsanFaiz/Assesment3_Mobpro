package com.ihsanfaiz0048.asessment3.navigation


const val KEY_ID_MENU = "idMenu"
const val KEY_ID_ORDER = "idOrder"

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object HistoryScreen: Screen("historyScreen")
    data object HistoryDetail: Screen("historyDetail/{$KEY_ID_MENU}") {
        fun withId(id: Long) = "historyDetail/$id"
    }
    data object DetailMenu : Screen(
        "detailScreen/{$KEY_ID_MENU}?$KEY_ID_ORDER={$KEY_ID_ORDER}"
    ) {

        fun create(
            idMenu: Long
        ) =
            "detailScreen/$idMenu"

        fun edit(
            idMenu: Long,
            idOrder: Long
        ) =
            "detailScreen/$idMenu?$KEY_ID_ORDER=$idOrder"
    }
}