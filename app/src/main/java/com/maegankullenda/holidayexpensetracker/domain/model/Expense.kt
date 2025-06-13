package com.maegankullenda.holidayexpensetracker.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

import com.maegankullenda.holidayexpensetracker.domain.model.Currency

data class Expense(
    val id: String,
    val holidayId: String,
    val amount: Double,
    val description: String,
    val category: ExpenseCategory,
    val date: LocalDate,
    val currency: Currency,
    val createdAt: LocalDateTime = LocalDateTime.now()
) 