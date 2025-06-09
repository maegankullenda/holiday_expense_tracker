package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class ExpenseHistoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun expenseHistoryScreen_displaysExpenses() {
        // Given
        val expenses = listOf(
            Expense(
                id = "1",
                holidayId = "1",
                amount = 200.0,
                description = "Dinner",
                category = ExpenseCategory.FOOD,
                date = LocalDate.of(2024, 1, 1),
                currency = Currency.ZAR,
            ),
            Expense(
                id = "2",
                holidayId = "1",
                amount = 300.0,
                description = "Hotel",
                category = ExpenseCategory.ACCOMMODATION,
                date = LocalDate.of(2024, 1, 2),
                currency = Currency.ZAR,
            ),
        )
        val state = ExpenseHistoryState(
            expenses = expenses,
            isLoading = false,
            error = null,
        )

        // When
        composeTestRule.setContent {
            ExpenseHistoryScreen(
                state = state,
                onNavigateBack = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("Dinner").assertIsDisplayed()
        composeTestRule.onNodeWithText("R200.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("FOOD").assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-01").assertIsDisplayed()

        composeTestRule.onNodeWithText("Hotel").assertIsDisplayed()
        composeTestRule.onNodeWithText("R300.0").assertIsDisplayed()
        composeTestRule.onNodeWithText("ACCOMMODATION").assertIsDisplayed()
        composeTestRule.onNodeWithText("2024-01-02").assertIsDisplayed()
    }

    @Test
    fun expenseHistoryScreen_displaysEmptyState() {
        // Given
        val state = ExpenseHistoryState(
            expenses = emptyList(),
            isLoading = false,
            error = null,
        )

        // When
        composeTestRule.setContent {
            ExpenseHistoryScreen(
                state = state,
                onNavigateBack = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("No expenses yet").assertIsDisplayed()
    }

    @Test
    fun expenseHistoryScreen_displaysLoadingState() {
        // Given
        val state = ExpenseHistoryState(
            expenses = emptyList(),
            isLoading = true,
            error = null,
        )

        // When
        composeTestRule.setContent {
            ExpenseHistoryScreen(
                state = state,
                onNavigateBack = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("Loading...").assertIsDisplayed()
    }

    @Test
    fun expenseHistoryScreen_displaysErrorState() {
        // Given
        val state = ExpenseHistoryState(
            expenses = emptyList(),
            isLoading = false,
            error = "Failed to load expenses",
        )

        // When
        composeTestRule.setContent {
            ExpenseHistoryScreen(
                state = state,
                onNavigateBack = {},
            )
        }

        // Then
        composeTestRule.onNodeWithText("Failed to load expenses").assertIsDisplayed()
    }
} 