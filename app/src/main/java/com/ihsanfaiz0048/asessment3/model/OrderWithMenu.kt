package com.ihsanfaiz0048.asessment3.model

import androidx.room.Embedded

data class OrderWithMenu(
    @Embedded
    val order: Order,

    @Embedded(prefix = "menu_")
    val menu: Menu
)
