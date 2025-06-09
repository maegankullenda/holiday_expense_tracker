package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import org.junit.Rule
import org.junit.Test

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
} 