package com.ihsanfaiz0048.asessment3.util

import com.ihsanfaiz0048.asessment3.model.OrderWithMenu

fun OrderWithMenu.getStatus(currentTime: Long): OrderStatus{
    val now = System.currentTimeMillis()

    val elapsed = currentTime - order.createdAt

    return when {

        elapsed < 30_000 ->
            OrderStatus.PENDING

        elapsed < 60_000 ->
            OrderStatus.PROCESSING

        else ->
            OrderStatus.COMPLETED
    }
}