package com.maegankullenda.holidayexpensetracker.presentation.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ExpensesState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(ExpensesState())
    val state: StateFlow<ExpensesState> = _state

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        getExpensesUseCase("current") // TODO: Get from current holiday
            .onStart { _state.update { it.copy(isLoading = true) } }
            .onEach { expenses ->
                _state.update {
                    it.copy(
                        expenses = expenses,
                        isLoading = false,
                        error = null,
                    )
                }
            }
            .catch { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error",
                    )
                }
            }
            .launchIn(viewModelScope)
    }
} 