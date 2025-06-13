package com.maegankullenda.holidayexpensetracker.presentation.ui.budget

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.usecase.GetBudgetSummaryUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageExpenseUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class BudgetState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalBudget: Double = 0.0,
    val spentAmount: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val dailySpend: Double = 0.0,
    val amount: String = "",
    val description: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.OTHER,
    val currentHoliday: com.maegankullenda.holidayexpensetracker.domain.model.Holiday? = null,
    val currency: Currency = Currency.ZAR
)

sealed class BudgetEvent {
    data class OnAmountChange(val amount: String) : BudgetEvent()
    data class OnDescriptionChange(val description: String) : BudgetEvent()
    data class OnCategorySelect(val category: String) : BudgetEvent()
    object OnAddExpense : BudgetEvent()
    object OnDismissError : BudgetEvent()
}

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetSummaryUseCase: GetBudgetSummaryUseCase,
    private val manageExpenseUseCase: ManageExpenseUseCase,
    private val manageHolidayUseCase: ManageHolidayUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    init {
        loadBudgetSummary()
        loadCurrentHoliday()
    }

    private fun loadBudgetSummary() {
        viewModelScope.launch {
            getBudgetSummaryUseCase()
                .onStart { _state.update { it.copy(isLoading = true) } }
                .catch { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
                .collect { summary ->
                    summary?.let { safeSummary ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                totalBudget = safeSummary.totalBudget,
                                spentAmount = safeSummary.spentAmount,
                                remainingAmount = safeSummary.remainingAmount,
                                dailySpend = calculateDailySpend(safeSummary)
                            )
                        }
                    }
                }
        }
    }

    private fun calculateDailySpend(summary: BudgetSummary): Double {
        val today = LocalDate.now()
        return summary.expenses
            .filter { expense -> expense.date == today }
            .sumOf { expense -> expense.amount }
    }

    fun loadCurrentHoliday() {
        viewModelScope.launch {
            try {
                manageHolidayUseCase.getCurrentHolidayStream().collect { holiday ->
                    _state.update { it.copy(currentHoliday = holiday) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to load current holiday") }
            }
        }
    }

    fun onEvent(event: BudgetEvent) {
        when (event) {
            is BudgetEvent.OnAmountChange -> updateAmount(event.amount)
            is BudgetEvent.OnDescriptionChange -> updateDescription(event.description)
            is BudgetEvent.OnCategorySelect -> updateCategory(event.category)
            is BudgetEvent.OnAddExpense -> addExpense()
            is BudgetEvent.OnDismissError -> dismissError()
        }
    }

    private fun updateAmount(amount: String) {
        _state.update { it.copy(amount = amount) }
    }

    private fun updateDescription(description: String) {
        _state.update { it.copy(description = description) }
    }

    private fun updateCategory(category: String) {
        _state.update { it.copy(selectedCategory = ExpenseCategory.valueOf(category)) }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    private fun addExpense() {
        viewModelScope.launch {
            try {
                val amountDouble = _state.value.amount.toDoubleOrNull()
                if (amountDouble == null) {
                    _state.update { it.copy(error = "Invalid amount") }
                    return@launch
                }

                val currentHoliday = _state.value.currentHoliday
                if (currentHoliday == null) {
                    _state.update { it.copy(error = "No active holiday") }
                    return@launch
                }

                val expense = Expense(
                    id = UUID.randomUUID().toString(),
                    holidayId = currentHoliday.id,
                    amount = amountDouble,
                    description = _state.value.description,
                    category = _state.value.selectedCategory,
                    date = LocalDate.now(),
                    currency = currentHoliday.currency,
                )

                manageExpenseUseCase.addExpense(expense)
                
                // Reset form
                _state.update {
                    it.copy(
                        amount = "",
                        description = "",
                        selectedCategory = ExpenseCategory.OTHER,
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to add expense") }
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                manageExpenseUseCase.deleteExpense(expense)
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to delete expense") }
            }
        }
    }
} 