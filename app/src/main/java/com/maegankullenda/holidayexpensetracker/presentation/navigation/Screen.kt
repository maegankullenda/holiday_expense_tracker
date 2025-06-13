package com.maegankullenda.holidayexpensetracker.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateHoliday : Screen("create_holiday")
    object HolidayDetails : Screen("holiday_details/{holidayId}") {
        fun createRoute(holidayId: String) = "holiday_details/$holidayId"
    }
    object AddExpense : Screen("add_expense/{holidayId}") {
        fun createRoute(holidayId: String) = "add_expense/$holidayId"
    }
    object Settings : Screen("settings")
    object ExpenseHistory : Screen("expense_history")
    object DailySpendSummary : Screen("daily_spend_summary")
} 