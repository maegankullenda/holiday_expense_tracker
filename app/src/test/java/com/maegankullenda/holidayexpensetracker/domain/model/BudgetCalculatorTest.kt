package com.maegankullenda.holidayexpensetracker.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class BudgetCalculatorTest {
    private val fixedDate = LocalDate.of(2024, 1, 1)
    private val fixedClock = Clock.fixed(
        fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant(),
        ZoneId.systemDefault()
    )
    private val calculator = BudgetCalculator(fixedClock)

    @Test
    fun `calculate daily allowance with no expenses`() {
        // Given
        val holiday = Holiday(
            id = "1",
            name = "Spain Holiday",
            destination = "Spain",
            startDate = fixedDate,
            endDate = fixedDate.plusDays(19),
            totalBudget = 2000.0,
            currency = Currency.ZAR,
            isActive = true,
        )
        val expenses = emptyList<Expense>()

        // When
        val result = calculator.calculateBudgetSummary(holiday, expenses)

        // Then
        assertEquals(2000.0, result.totalBudget, 0.01)
        assertEquals(100.0, result.dailyBudget, 0.01)
        assertEquals(2000.0, result.remainingBudget, 0.01)
        assertEquals(20, result.remainingDays)
        assertEquals(0.0, result.spentToday, 0.01)
        assertEquals(0.0, result.totalSpent, 0.01)
    }

    @Test
    fun `calculate daily allowance with one expense`() {
        // Given
        val holiday = Holiday(
            id = "1",
            name = "Spain Holiday",
            destination = "Spain",
            startDate = fixedDate,
            endDate = fixedDate.plusDays(19),
            totalBudget = 2000.0,
            currency = Currency.ZAR,
            isActive = true,
        )
        val expenses = listOf(
            Expense(
                id = "1",
                holidayId = "1",
                amount = 200.0,
                description = "Dinner",
                category = ExpenseCategory.FOOD,
                date = fixedDate,
                currency = Currency.ZAR,
            ),
        )

        // When
        val result = calculator.calculateBudgetSummary(holiday, expenses)

        // Then
        assertEquals(2000.0, result.totalBudget, 0.01)
        assertEquals(90.0, result.dailyBudget, 0.01)
        assertEquals(1800.0, result.remainingBudget, 0.01)
        assertEquals(20, result.remainingDays)
        assertEquals(200.0, result.spentToday, 0.01)
        assertEquals(200.0, result.totalSpent, 0.01)
    }

    @Test
    fun `calculate daily allowance with multiple expenses`() {
        // Given
        val holiday = Holiday(
            id = "1",
            name = "Spain Holiday",
            destination = "Spain",
            startDate = fixedDate,
            endDate = fixedDate.plusDays(19),
            totalBudget = 2000.0,
            currency = Currency.ZAR,
            isActive = true,
        )
        val expenses = listOf(
            Expense(
                id = "1",
                holidayId = "1",
                amount = 200.0,
                description = "Dinner",
                category = ExpenseCategory.FOOD,
                date = fixedDate,
                currency = Currency.ZAR,
            ),
            Expense(
                id = "2",
                holidayId = "1",
                amount = 300.0,
                description = "Hotel",
                category = ExpenseCategory.ACCOMMODATION,
                date = fixedDate.plusDays(1),
                currency = Currency.ZAR,
            ),
        )

        // When
        val result = calculator.calculateBudgetSummary(holiday, expenses)

        // Then
        assertEquals(2000.0, result.totalBudget, 0.01)
        assertEquals(75.0, result.dailyBudget, 0.01)
        assertEquals(1500.0, result.remainingBudget, 0.01)
        assertEquals(20, result.remainingDays)
        assertEquals(200.0, result.spentToday, 0.01)
        assertEquals(500.0, result.totalSpent, 0.01)
    }
} 