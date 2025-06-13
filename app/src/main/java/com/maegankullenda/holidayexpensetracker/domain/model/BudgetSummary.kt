package com.maegankullenda.holidayexpensetracker.domain.model

import java.time.LocalDate

data class BudgetSummary(
    val holidayId: String,
    val totalBudget: Double,
    val spentAmount: Double,
    val remainingAmount: Double,
    val dailyBudget: Double,
    val remainingDays: Int,
    val expenses: List<Expense>,
    val currency: Currency
) 