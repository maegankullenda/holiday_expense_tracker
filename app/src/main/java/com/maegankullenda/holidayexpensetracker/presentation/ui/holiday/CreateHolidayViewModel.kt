package com.maegankullenda.holidayexpensetracker.presentation.ui.holiday

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class CreateHolidayState(
    val name: String = "",
    val destination: String = "",
    val totalBudget: String = "",
    val selectedCurrency: Currency = Currency.ZAR,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now().plusDays(7),
    val isCurrencyDropdownExpanded: Boolean = false,
    val showDatePicker: Boolean = false,
    val isStartDateSelection: Boolean = true,
    val isCreated: Boolean = false,
    val error: String? = null
)

sealed interface CreateHolidayEvent {
    data class OnNameChange(val name: String) : CreateHolidayEvent
    data class OnDestinationChange(val destination: String) : CreateHolidayEvent
    data class OnTotalBudgetChange(val totalBudget: String) : CreateHolidayEvent
    data class OnCurrencySelect(val currency: Currency) : CreateHolidayEvent
    data class OnCurrencyDropdownExpandedChange(val expanded: Boolean) : CreateHolidayEvent
    object OnStartDateClick : CreateHolidayEvent
    object OnEndDateClick : CreateHolidayEvent
    object OnDatePickerDismiss : CreateHolidayEvent
    data class OnDateSelected(val date: LocalDate) : CreateHolidayEvent
    object OnSubmit : CreateHolidayEvent
}

@HiltViewModel
class CreateHolidayViewModel @Inject constructor(
    private val manageHolidayUseCase: ManageHolidayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateHolidayState())
    val state: StateFlow<CreateHolidayState> = _state

    fun onEvent(event: CreateHolidayEvent) {
        when (event) {
            is CreateHolidayEvent.OnNameChange -> {
                _state.update { it.copy(name = event.name) }
            }
            is CreateHolidayEvent.OnDestinationChange -> {
                _state.update { it.copy(destination = event.destination) }
            }
            is CreateHolidayEvent.OnTotalBudgetChange -> {
                _state.update { it.copy(totalBudget = event.totalBudget) }
            }
            is CreateHolidayEvent.OnCurrencySelect -> {
                _state.update { it.copy(selectedCurrency = event.currency) }
            }
            is CreateHolidayEvent.OnCurrencyDropdownExpandedChange -> {
                _state.update { it.copy(isCurrencyDropdownExpanded = event.expanded) }
            }
            is CreateHolidayEvent.OnStartDateClick -> {
                _state.update { 
                    it.copy(
                        showDatePicker = true,
                        isStartDateSelection = true
                    )
                }
            }
            is CreateHolidayEvent.OnEndDateClick -> {
                _state.update { 
                    it.copy(
                        showDatePicker = true,
                        isStartDateSelection = false
                    )
                }
            }
            is CreateHolidayEvent.OnDatePickerDismiss -> {
                _state.update { it.copy(showDatePicker = false) }
            }
            is CreateHolidayEvent.OnDateSelected -> {
                if (_state.value.isStartDateSelection) {
                    _state.update { it.copy(startDate = event.date) }
                } else {
                    _state.update { it.copy(endDate = event.date) }
                }
                _state.update { it.copy(showDatePicker = false) }
            }
            is CreateHolidayEvent.OnSubmit -> submitHoliday()
        }
    }

    private fun submitHoliday() {
        viewModelScope.launch {
            try {
                val state = _state.value
                
                // Validate input
                if (state.name.isBlank()) {
                    _state.update { it.copy(error = "Please enter a holiday name") }
                    return@launch
                }
                if (state.destination.isBlank()) {
                    _state.update { it.copy(error = "Please enter a destination") }
                    return@launch
                }
                val totalBudget = state.totalBudget.toDoubleOrNull()
                if (totalBudget == null || totalBudget <= 0) {
                    _state.update { it.copy(error = "Please enter a valid budget amount") }
                    return@launch
                }
                if (state.endDate.isBefore(state.startDate)) {
                    _state.update { it.copy(error = "End date cannot be before start date") }
                    return@launch
                }

                val holiday = Holiday(
                    id = UUID.randomUUID().toString(),
                    name = state.name,
                    destination = state.destination,
                    startDate = state.startDate,
                    endDate = state.endDate,
                    totalBudget = totalBudget,
                    currency = state.selectedCurrency,
                    isActive = true
                )

                manageHolidayUseCase.createHoliday(holiday)
                manageHolidayUseCase.setActiveHoliday(holiday.id)

                // Update state to trigger navigation
                _state.update { it.copy(isCreated = true) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message ?: "Failed to create holiday") }
            }
        }
    }
} 