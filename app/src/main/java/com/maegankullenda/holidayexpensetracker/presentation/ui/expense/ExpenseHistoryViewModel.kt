package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.usecase.GetExpensesUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageExpenseUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpenseHistoryState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val expenseToDelete: Expense? = null
)

sealed interface ExpenseHistoryEvent {
    data class DeleteExpense(val expense: Expense) : ExpenseHistoryEvent
    data object ConfirmDelete : ExpenseHistoryEvent
    data object DismissDeleteDialog : ExpenseHistoryEvent
}

@HiltViewModel
class ExpenseHistoryViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val manageExpenseUseCase: ManageExpenseUseCase,
    private val manageHolidayUseCase: ManageHolidayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ExpenseHistoryState(isLoading = true))
    val state: StateFlow<ExpenseHistoryState> = _state

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            try {
                val currentHoliday = manageHolidayUseCase.getCurrentHolidayStream().first()
                if (currentHoliday == null) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "No active holiday found"
                        )
                    }
                    return@launch
                }

                getExpensesUseCase(currentHoliday.id).collect { expenses ->
                    _state.update {
                        it.copy(
                            expenses = expenses.sortedByDescending { expense -> expense.date },
                            isLoading = false,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load expenses"
                    )
                }
            }
        }
    }

    fun onEvent(event: ExpenseHistoryEvent) {
        when (event) {
            is ExpenseHistoryEvent.DeleteExpense -> {
                _state.update {
                    it.copy(
                        showDeleteConfirmation = true,
                        expenseToDelete = event.expense
                    )
                }
            }
            is ExpenseHistoryEvent.ConfirmDelete -> {
                deleteExpense()
            }
            is ExpenseHistoryEvent.DismissDeleteDialog -> {
                _state.update {
                    it.copy(
                        showDeleteConfirmation = false,
                        expenseToDelete = null
                    )
                }
            }
        }
    }

    private fun deleteExpense() {
        val expenseToDelete = state.value.expenseToDelete ?: return
        
        viewModelScope.launch {
            try {
                manageExpenseUseCase.deleteExpense(expenseToDelete)
                _state.update {
                    it.copy(
                        showDeleteConfirmation = false,
                        expenseToDelete = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message ?: "Failed to delete expense",
                        showDeleteConfirmation = false,
                        expenseToDelete = null
                    )
                }
            }
        }
    }
} 