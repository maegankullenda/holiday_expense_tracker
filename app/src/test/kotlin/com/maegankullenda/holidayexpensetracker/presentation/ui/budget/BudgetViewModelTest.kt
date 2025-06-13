package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import com.maegankullenda.holidayexpensetracker.domain.model.*
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import com.maegankullenda.holidayexpensetracker.domain.usecase.GetBudgetSummaryUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageExpenseUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class BudgetViewModelTest {

    private lateinit var viewModel: BudgetViewModel
    private lateinit var holidayRepository: HolidayRepository
    private lateinit var expenseRepository: ExpenseRepository
    private lateinit var getBudgetSummaryUseCase: GetBudgetSummaryUseCase
    private lateinit var manageHolidayUseCase: ManageHolidayUseCase
    private lateinit var manageExpenseUseCase: ManageExpenseUseCase

    private val testDispatcher = UnconfinedTestDispatcher()
    private val fixedDate = LocalDate.of(2024, 1, 1)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        holidayRepository = mockk(relaxed = true)
        expenseRepository = mockk(relaxed = true)
        getBudgetSummaryUseCase = mockk(relaxed = true)
        manageHolidayUseCase = mockk(relaxed = true)
        manageExpenseUseCase = mockk(relaxed = true)

        viewModel = BudgetViewModel(
            getBudgetSummaryUseCase = getBudgetSummaryUseCase,
            manageHolidayUseCase = manageHolidayUseCase,
            manageExpenseUseCase = manageExpenseUseCase,
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when holiday is active, budget summary is updated`() = runTest {
        // Given
        val budgetSummary = BudgetSummary(
            holidayId = "1",
            totalBudget = 2000.0,
            spentAmount = 200.0,
            remainingAmount = 1800.0,
            dailyBudget = 90.0,
            remainingDays = 20,
            expenses = emptyList(),
            currency = Currency.ZAR
        )

        every { getBudgetSummaryUseCase() } returns flowOf(budgetSummary)
        every { manageHolidayUseCase.getCurrentHolidayStream() } returns flowOf(null)
        coEvery { manageExpenseUseCase.addExpense(any()) } returns Unit

        // When
        val method = BudgetViewModel::class.java.getDeclaredMethod("loadBudgetSummary")
        method.isAccessible = true
        method.invoke(viewModel)
        delay(100) // Wait for state to be updated

        // Then
        println("Expected totalBudget: ${budgetSummary.totalBudget}, Actual: ${viewModel.state.value.totalBudget}")
        println("Expected spentAmount: ${budgetSummary.spentAmount}, Actual: ${viewModel.state.value.spentAmount}")
        println("Expected remainingAmount: ${budgetSummary.remainingAmount}, Actual: ${viewModel.state.value.remainingAmount}")
        assertEquals(budgetSummary.totalBudget, viewModel.state.value.totalBudget, 0.01)
        assertEquals(budgetSummary.spentAmount, viewModel.state.value.spentAmount, 0.01)
        assertEquals(budgetSummary.remainingAmount, viewModel.state.value.remainingAmount, 0.01)
    }

    @Test
    fun `when adding expense, repository is updated`() = runTest {
        // Given
        val amount = "200.0"
        val description = "Dinner"
        val category = ExpenseCategory.FOOD
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

        every { manageHolidayUseCase.getCurrentHolidayStream() } returns flowOf(holiday)
        coEvery { manageExpenseUseCase.addExpense(any()) } returns Unit

        // When
        viewModel.loadCurrentHoliday()
        viewModel.onEvent(BudgetEvent.OnAmountChange(amount))
        viewModel.onEvent(BudgetEvent.OnDescriptionChange(description))
        viewModel.onEvent(BudgetEvent.OnCategorySelect(category.name))
        viewModel.onEvent(BudgetEvent.OnAddExpense)

        // Then
        coVerify { manageExpenseUseCase.addExpense(any()) }
    }
} 