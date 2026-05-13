package com.ihsanfaiz0048.assesment2mobpro.model

import androidx.room.Embedded

data class OrderWithMenu(
    @Embedded
    val order: Order,

    @Embedded(prefix = "menu_")
    val menu: Menu
)
