package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import io.mockk.every
import io.mockk.mockk

class BudgetScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun budgetScreen_displaysCorrectBudgetInformation() {
        // Given
        val budgetSummary = BudgetSummary(
            totalBudget = 2000.0,
            remainingBudget = 1800.0,
            dailyAllowance = 94.74,
            remainingDays = 19,
            currency = Currency.ZAR,
        )
        val state = BudgetState(
            budgetSummary = budgetSummary,
            isLoading = false,
            error = null,
        )

        // When
        composeTestRule.setContent {
            BudgetScreen(
                state = state,
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("R2000.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("R1800.00").assertIsDisplayed()
        composeTestRule.onNodeWithText("R94.74").assertIsDisplayed()
        composeTestRule.onNodeWithText("19").assertIsDisplayed()
    }

    @Test
    fun budgetScreen_displaysLoadingState() {
        // Given
        val state = BudgetState(
            budgetSummary = null,
            isLoading = true,
            error = null,
        )

        // When
        composeTestRule.setContent {
            BudgetScreen(
                state = state,
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun budgetScreen_displaysErrorState() {
        // Given
        val state = BudgetState(
            budgetSummary = null,
            isLoading = false,
            error = "Failed to load budget",
        )

        // When
        composeTestRule.setContent {
            BudgetScreen(
                state = state,
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("Failed to load budget").assertIsDisplayed()
    }

    @Test
    fun whenHolidayCurrencyIsZAR_budgetScreenShowsZAR() {
        val viewModel = createMockBudgetViewModel(
            BudgetState(
                totalBudget = 1000.0,
                spentAmount = 500.0,
                remainingAmount = 500.0,
                dailySpend = 100.0,
                currency = Currency.ZAR
            )
        )
        composeTestRule.setContent {
            BudgetScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
                onNavigateToDailySpendSummary = {}
            )
        }
        // Add assertions here
    }

    @Test
    fun whenHolidayCurrencyIsUSD_budgetScreenShowsUSD() {
        val viewModel = createMockBudgetViewModel(
            BudgetState(
                totalBudget = 1000.0,
                spentAmount = 500.0,
                remainingAmount = 500.0,
                dailySpend = 100.0,
                currency = Currency.USD
            )
        )
        composeTestRule.setContent {
            BudgetScreen(
                viewModel = viewModel,
                onNavigateBack = {},
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
                onNavigateToDailySpendSummary = {}
            )
        }
        // Add assertions here
    }
}

private fun createMockBudgetViewModel(state: BudgetState): BudgetViewModel {
    val mock = mockk<BudgetViewModel>(relaxed = true)
    val stateFlow = MutableStateFlow(state)
    every { mock.state } returns stateFlow
    return mock
} 