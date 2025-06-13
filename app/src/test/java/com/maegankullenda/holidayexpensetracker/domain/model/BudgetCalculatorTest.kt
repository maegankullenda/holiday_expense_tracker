package com.maegankullenda.holidayexpensetracker.domain.model

import app.cash.turbine.test
import com.maegankullenda.holidayexpensetracker.data.repository.BudgetRepositoryImpl
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

class BudgetCalculatorTest {
    private lateinit var holidayRepository: HolidayRepository
    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var budgetRepository: BudgetRepositoryImpl
    private val fixedDate = LocalDate.of(2024, 1, 1)
    private val fixedClock = Clock.fixed(fixedDate.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

    @Before
    fun setUp() {
        holidayRepository = mockk()
        expenseRepository = mockk()
        budgetRepository = BudgetRepositoryImpl(holidayRepository, expenseRepository, fixedClock)
    }

    @Test
    fun `calculate daily allowance with no expenses`() = runBlocking {
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

        coEvery { holidayRepository.getCurrentHolidayStream() } returns flowOf(holiday)
        coEvery { expenseRepository.getExpensesStream() } returns flowOf(emptyList())

        // When & Then
        budgetRepository.getBudgetSummary().test {
            val summary = awaitItem()!!
            assertEquals(2000.0, summary.totalBudget, 0.01)
            assertEquals(100.0, summary.dailyBudget, 0.01)
            assertEquals(2000.0, summary.remainingAmount, 0.01)
            assertEquals(20, summary.remainingDays)
            assertEquals(0.0, summary.spentAmount, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `calculate daily allowance with one expense`() = runBlocking {
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

        coEvery { holidayRepository.getCurrentHolidayStream() } returns flowOf(holiday)
        coEvery { expenseRepository.getExpensesStream() } returns flowOf(expenses)

        // When & Then
        budgetRepository.getBudgetSummary().test {
            val summary = awaitItem()!!
            assertEquals(2000.0, summary.totalBudget, 0.01)
            assertEquals(90.0, summary.dailyBudget, 0.01)
            assertEquals(1800.0, summary.remainingAmount, 0.01)
            assertEquals(20, summary.remainingDays)
            assertEquals(200.0, summary.spentAmount, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `calculate daily allowance with multiple expenses`() = runBlocking {
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

        coEvery { holidayRepository.getCurrentHolidayStream() } returns flowOf(holiday)
        coEvery { expenseRepository.getExpensesStream() } returns flowOf(expenses)

        // When & Then
        budgetRepository.getBudgetSummary().test {
            val summary = awaitItem()!!
            assertEquals(2000.0, summary.totalBudget, 0.01)
            assertEquals(75.0, summary.dailyBudget, 0.01)
            assertEquals(1500.0, summary.remainingAmount, 0.01)
            assertEquals(20, summary.remainingDays)
            assertEquals(500.0, summary.spentAmount, 0.01)
            cancelAndIgnoreRemainingEvents()
        }
    }
} 