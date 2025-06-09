package com.maegankullenda.holidayexpensetracker.presentation.ui.expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageExpenseUseCase
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class AddExpenseState(
    val amount: String = "",
    val description: String = "",
    val selectedCategory: ExpenseCategory = ExpenseCategory.OTHER,
    val isCategoryDropdownExpanded: Boolean = false,
    val isExpenseAdded: Boolean = false,
    val error: String? = null
)

sealed interface AddExpenseEvent {
    data class OnAmountChange(val amount: String) : AddExpenseEvent
    data class OnDescriptionChange(val description: String) : AddExpenseEvent
    data class OnCategorySelect(val category: ExpenseCategory) : AddExpenseEvent
    data class OnCategoryDropdownExpandedChange(val expanded: Boolean) : AddExpenseEvent
    data object OnSubmit : AddExpenseEvent
}

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val manageExpenseUseCase: ManageExpenseUseCase,
    private val manageHolidayUseCase: ManageHolidayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddExpenseState())
    val state: StateFlow<AddExpenseState> = _state

    fun onEvent(event: AddExpenseEvent) {
        when (event) {
            is AddExpenseEvent.OnAmountChange -> {
                _state.update { it.copy(amount = event.amount) }
            }
            is AddExpenseEvent.OnDescriptionChange -> {
                _state.update { it.copy(description = event.description) }
            }
            is AddExpenseEvent.OnCategorySelect -> {
                _state.update { it.copy(selectedCategory = event.category) }
            }
            is AddExpenseEvent.OnCategoryDropdownExpandedChange -> {
                _state.update { it.copy(isCategoryDropdownExpanded = event.expanded) }
            }
            is AddExpenseEvent.OnSubmit -> submitExpense()
        }
    }

    private fun submitExpense() {
        viewModelScope.launch {
            try {
                val state = _state.value
                
                // Validate input
                if (state.amount.isBlank()) {
                    _state.update { it.copy(error = "Please enter an amount") }
                    return@launch
                }
                val amount = state.amount.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    _state.update { it.copy(error = "Please enter a valid amount") }
                    return@launch
                }
                if (state.description.isBlank()) {
                    _state.update { it.copy(error = "Please enter a description") }
                    return@launch
                }

                // Get current holiday
                val currentHoliday = manageHolidayUseCase.getCurrentHolidayStream().first()
                if (currentHoliday == null) {
                    _state.update { it.copy(error = "No active holiday found") }
                    return@launch
                }

                val expense = Expense(
                    id = UUID.randomUUID().toString(),
                    holidayId = currentHoliday.id,
                    amount = amount,
                    description = state.description,
                    category = state.selectedCategory,
                    date = LocalDate.now(),
                    currency = currentHoliday.currency
                )

                manageExpenseUseCase.addExpense(expense)
                
                _state.update { it.copy(isExpenseAdded = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to add expense") }
            }
        }
    }
} 