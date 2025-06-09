package com.maegankullenda.holidayexpensetracker.domain.model

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Clock
import java.time.LocalDate
import javax.inject.Inject

data class BudgetSummary(
    val totalBudget: Double,
    val remainingBudget: Double,
    val dailyBudget: Double,
    val remainingDays: Int,
    val spentToday: Double,
    val totalSpent: Double,
    val currency: Currency,
)

class BudgetCalculator @Inject constructor(
    private val clock: Clock = Clock.systemDefaultZone()
) {
    fun calculateBudgetSummary(
        holiday: Holiday,
        expenses: List<Expense>,
    ): BudgetSummary {
        val today = LocalDate.now(clock)
        val totalSpent = expenses.sumOf { it.amount }
        val remainingBudget = holiday.totalBudget - totalSpent
        val spentToday = expenses
            .filter { it.date == today }
            .sumOf { it.amount }

        // Calculate total days of the holiday
        val totalDays = holiday.endDate.toEpochDay().minus(holiday.startDate.toEpochDay()).toInt() + 1

        // Calculate remaining days based on today's date
        val remainingDays = if (today.isBefore(holiday.startDate)) {
            totalDays // Holiday hasn't started yet
        } else if (today.isAfter(holiday.endDate)) {
            0 // Holiday is over
        } else {
            holiday.endDate.toEpochDay().minus(today.toEpochDay()).toInt() + 1
        }

        // Calculate daily budget based on the current state of the holiday
        val dailyBudget = when {
            today.isBefore(holiday.startDate) -> {
                // Holiday hasn't started - divide total budget by total days
                (holiday.totalBudget / totalDays)
                    .toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()
            }
            remainingDays > 0 -> {
                // Holiday is ongoing - divide remaining budget by remaining days
                (remainingBudget / remainingDays)
                    .toBigDecimal()
                    .setScale(2, RoundingMode.HALF_UP)
                    .toDouble()
            }
            else -> 0.0 // Holiday is over
        }

        return BudgetSummary(
            totalBudget = holiday.totalBudget,
            remainingBudget = remainingBudget,
            dailyBudget = dailyBudget,
            remainingDays = remainingDays,
            spentToday = spentToday,
            totalSpent = totalSpent,
            currency = holiday.currency,
        )
    }
} 