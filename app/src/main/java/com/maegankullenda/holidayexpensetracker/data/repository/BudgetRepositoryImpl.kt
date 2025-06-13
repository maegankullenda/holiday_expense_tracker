package com.maegankullenda.holidayexpensetracker.data.repository

import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.repository.BudgetRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    private val holidayRepository: HolidayRepository,
    private val expenseRepository: ExpenseRepository,
    private val clock: Clock = Clock.systemDefaultZone()
) : BudgetRepository {
    override fun getBudgetSummary(): Flow<BudgetSummary?> {
        return holidayRepository.getCurrentHolidayStream().combine(
            expenseRepository.getExpensesStream()
        ) { holiday, expenses ->
            if (holiday == null) return@combine null
            val holidayExpenses = expenses.filter { it.holidayId == holiday.id }
            val spentAmount = holidayExpenses.sumOf { it.amount }
            val remainingAmount = holiday.totalBudget - spentAmount
            val today = LocalDate.now(clock)
            val dailySpend = holidayExpenses.filter { it.date == today }.sumOf { it.amount }
            val remainingDays = holiday.endDate.toEpochDay() - today.toEpochDay() + 1
            val dailyBudget = if (remainingDays > 0) remainingAmount / remainingDays else 0.0
            BudgetSummary(
                holidayId = holiday.id,
                totalBudget = holiday.totalBudget,
                spentAmount = spentAmount,
                remainingAmount = remainingAmount,
                dailyBudget = dailyBudget,
                remainingDays = remainingDays.toInt(),
                expenses = holidayExpenses,
                currency = holiday.currency
            )
        }
    }
} 