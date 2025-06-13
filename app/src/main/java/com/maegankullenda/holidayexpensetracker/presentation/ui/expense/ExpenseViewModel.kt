package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExpenseState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val expenses: List<Expense> = emptyList()
)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    
    private val _state = MutableStateFlow(ExpenseState())
    val state: StateFlow<ExpenseState> = _state.asStateFlow()
    
    init {
        loadExpenses()
    }
    
    private fun loadExpenses() {
        viewModelScope.launch {
            expenseRepository.getExpensesStream()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { expenses ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            expenses = expenses
                        )
                    }
                }
        }
    }
    
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.addExpense(expense)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.updateExpense(expense)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
    
    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }
} 