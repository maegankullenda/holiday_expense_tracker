package com.maegankullenda.holidayexpensetracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maegankullenda.holidayexpensetracker.presentation.ui.budget.BudgetScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.expense.AddExpenseScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.expense.ExpenseHistoryScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.holiday.HolidayListScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.holiday.CreateHolidayScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.budget.DailySpendSummaryScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "holiday_list"
    ) {
        composable("holiday_list") {
            HolidayListScreen(
                onNavigateToCreateHoliday = {
                    navController.navigate("create_holiday")
                },
                onHolidaySelected = {
                    navController.navigate("budget")
                }
            )
        }
        composable("create_holiday") {
            CreateHolidayScreen(
                onNavigateBack = { navController.popBackStack() },
                onHolidayCreated = { navController.popBackStack() }
            )
        }
        composable("budget") {
            BudgetScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAddExpense = { navController.navigate("add_expense") },
                onNavigateToDailySpendSummary = { navController.navigate("daily_spend_summary") }
            )
        }
        composable("add_expense") {
            AddExpenseScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("expense_history") {
            ExpenseHistoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("daily_spend_summary") {
            DailySpendSummaryScreen(
                navController = navController,
                onNavigateToExpenseHistory = { navController.navigate("expense_history") }
            )
        }
        // Add other navigation destinations as needed
    }
} 