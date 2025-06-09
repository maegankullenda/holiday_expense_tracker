package com.maegankullenda.holidayexpensetracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.maegankullenda.holidayexpensetracker.presentation.ui.budget.BudgetScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.expense.AddExpenseScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.expense.ExpenseHistoryScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.holiday.CreateHolidayScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.holiday.HolidayListScreen
import com.maegankullenda.holidayexpensetracker.presentation.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object HolidayList : Screen("holiday_list")
    object CreateHoliday : Screen("create_holiday")
    object Budget : Screen("budget")
    object ExpenseHistory : Screen("expense_history")
    object AddExpense : Screen("add_expense")
    object Settings : Screen("settings")
}

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HolidayList.route,
        modifier = modifier
    ) {
        composable(route = Screen.HolidayList.route) {
            HolidayListScreen(
                onNavigateToCreateHoliday = {
                    navController.navigate(Screen.CreateHoliday.route)
                },
                onHolidaySelected = {
                    navController.navigate(Screen.Budget.route)
                }
            )
        }

        composable(route = Screen.CreateHoliday.route) {
            CreateHolidayScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onHolidayCreated = {
                    navController.navigate(Screen.Budget.route) {
                        popUpTo(Screen.HolidayList.route)
                    }
                }
            )
        }

        composable(route = Screen.Budget.route) {
            BudgetScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddExpense = {
                    navController.navigate(Screen.AddExpense.route)
                },
                onNavigateToExpenseHistory = {
                    navController.navigate(Screen.ExpenseHistory.route)
                }
            )
        }

        composable(route = Screen.ExpenseHistory.route) {
            ExpenseHistoryScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(route = Screen.AddExpense.route) {
            AddExpenseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
} 