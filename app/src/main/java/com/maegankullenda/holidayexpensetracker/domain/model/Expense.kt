package com.maegankullenda.holidayexpensetracker.domain.model

import java.math.BigDecimal
import java.util.Date
import java.util.UUID
import java.time.LocalDate
import java.time.LocalDateTime

data class Expense(
    val id: String,
    val holidayId: String,
    val amount: Double,
    val description: String,
    val category: ExpenseCategory,
    val date: LocalDate,
    val currency: Currency,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

enum class ExpenseCategory {
    FOOD,
    ACCOMMODATION,
    TRANSPORTATION,
    ENTERTAINMENT,
    SHOPPING,
    SIGHTSEEING,
    OTHER,
} 