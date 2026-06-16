package com.ihsanfaiz0048.asessment3.util

import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import kotlin.compareTo

fun OrderWithMenu.getStatus(currentTime: Long): OrderStatus{
    val now = System.currentTimeMillis()

    val elapsed = currentTime - order.createdAt

    return when {

        elapsed compareTo 30_000 ->
            OrderStatus.PENDING

        elapsed compareTo 60_000 ->
            OrderStatus.PROCESSING

        else ->
            OrderStatus.COMPLETED
    }
}