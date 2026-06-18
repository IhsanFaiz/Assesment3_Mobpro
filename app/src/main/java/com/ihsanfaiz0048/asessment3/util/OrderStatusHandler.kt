package com.ihsanfaiz0048.asessment3.util

import com.ihsanfaiz0048.asessment3.model.OrderWithMenu
import java.time.OffsetDateTime

fun OrderWithMenu.getStatus(currentTime: Long): OrderStatus{
    val now = System.currentTimeMillis()

    val orderTime = try {
        OffsetDateTime.parse(order.tanggal).toInstant().toEpochMilli()
    } catch (e: Exception) {
        0L
    }

    val elapsed = currentTime - orderTime

    return when {

        elapsed < 30_000 ->
            OrderStatus.PENDING

        elapsed < 60_000 ->
            OrderStatus.PROCESSING

        else ->
            OrderStatus.COMPLETED
    }
}