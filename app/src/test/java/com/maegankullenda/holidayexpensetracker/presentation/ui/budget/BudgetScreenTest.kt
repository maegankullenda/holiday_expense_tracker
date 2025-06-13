package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.usecase.GetBudgetSummaryUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageExpenseUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Rule
import org.junit.Test

class BudgetScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase = mockk()
    private val manageExpenseUseCase: ManageExpenseUseCase = mockk()
    private val manageHolidayUseCase: ManageHolidayUseCase = mockk()
    private val navController: NavController = mockk()

    // Test temporarily disabled

    @Test
    fun whenHolidayCurrencyIsZAR_budgetScreenShowsZAR() {
        // Given
        val viewModel = FakeBudgetViewModel(
            totalBudget = 1000.0,
            spentAmount = 500.0,
            remainingAmount = 500.0,
            dailySpend = 100.0,
            currency = Currency.ZAR
        )

        // When
        composeTestRule.setContent {
            BudgetScreen(
                onNavigateBack = {},
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
                onNavigateToDailySpendSummary = {},
                viewModel = viewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("Total Budget").assertExists()
        composeTestRule.onNodeWithText("R1,000.00").assertExists()
        
        composeTestRule.onNodeWithText("Total Spent").assertExists()
        composeTestRule.onNodeWithText("R500.00").assertExists()
        
        composeTestRule.onNodeWithText("Remaining Budget").assertExists()
        composeTestRule.onNodeWithText("R500.00").assertExists()
        
        composeTestRule.onNodeWithText("Today's Spend").assertExists()
        composeTestRule.onNodeWithText("R100.00").assertExists()
    }

    @Test
    fun whenHolidayCurrencyIsUSD_budgetScreenShowsUSD() {
        // Given
        val viewModel = FakeBudgetViewModel(
            totalBudget = 1000.0,
            spentAmount = 500.0,
            remainingAmount = 500.0,
            dailySpend = 100.0,
            currency = Currency.USD
        )

        // When
        composeTestRule.setContent {
            BudgetScreen(
                onNavigateBack = {},
                onNavigateToAddExpense = {},
                onNavigateToExpenseHistory = {},
                onNavigateToDailySpendSummary = {},
                viewModel = viewModel
            )
        }

        // Then
        composeTestRule.onNodeWithText("Total Budget").assertExists()
        composeTestRule.onNodeWithText("$1,000.00").assertExists()
        
        composeTestRule.onNodeWithText("Total Spent").assertExists()
        composeTestRule.onNodeWithText("$500.00").assertExists()
        
        composeTestRule.onNodeWithText("Remaining Budget").assertExists()
        composeTestRule.onNodeWithText("$500.00").assertExists()
        
        composeTestRule.onNodeWithText("Today's Spend").assertExists()
        composeTestRule.onNodeWithText("$100.00").assertExists()
    }
}

private class FakeBudgetViewModel(
    private val totalBudget: Double,
    private val spentAmount: Double,
    private val remainingAmount: Double,
    private val dailySpend: Double,
    private val currency: Currency
) : BudgetViewModel(null, null) {
    override val state = BudgetState(
        totalBudget = totalBudget,
        spentAmount = spentAmount,
        remainingAmount = remainingAmount,
        dailySpend = dailySpend,
        currency = currency
    )
} 