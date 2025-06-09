package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import com.maegankullenda.holidayexpensetracker.domain.model.Holiday

data class BudgetState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentHoliday: Holiday? = null,
    val budgetSummary: BudgetSummary? = null,
    val amount: String = "",
    val description: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.OTHER,
)

sealed interface BudgetEvent {
    data class OnAmountChange(val amount: String) : BudgetEvent
    data class OnDescriptionChange(val description: String) : BudgetEvent
    data object OnAddExpense : BudgetEvent
    data class OnCategorySelect(val category: String) : BudgetEvent
    data object OnDismissError : BudgetEvent
} 