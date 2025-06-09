package com.maegankullenda.holidayexpensetracker.domain.model

import java.time.LocalDate

data class Holiday(
    val id: String,
    val name: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val totalBudget: Double,
    val currency: Currency = Currency.ZAR,
    val isActive: Boolean = false,
) {
    val totalDays: Int
        get() = endDate.toEpochDay().minus(startDate.toEpochDay()).toInt() + 1

    val remainingDays: Int
        get() = endDate.toEpochDay().minus(LocalDate.now().toEpochDay()).toInt() + 1
} 