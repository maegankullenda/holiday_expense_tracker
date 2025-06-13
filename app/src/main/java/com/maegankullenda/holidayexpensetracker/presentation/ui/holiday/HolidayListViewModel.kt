package com.maegankullenda.holidayexpensetracker.presentation.ui.holiday

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import com.maegankullenda.holidayexpensetracker.domain.usecase.ManageHolidayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HolidayListState(
    val holidays: List<Holiday> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDeleteConfirmation: Boolean = false,
    val holidayToDelete: Holiday? = null
)

sealed interface HolidayListEvent {
    data class DeleteHoliday(val holiday: Holiday) : HolidayListEvent
    data object ConfirmDelete : HolidayListEvent
    data object DismissDeleteDialog : HolidayListEvent
}

@HiltViewModel
class HolidayListViewModel @Inject constructor(
    private val manageHolidayUseCase: ManageHolidayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HolidayListState(isLoading = true))
    val state: StateFlow<HolidayListState> = _state

    init {
        Log.d("HolidayListViewModel", "Initializing ViewModel")
        loadHolidays()
    }

    private fun loadHolidays() {
        Log.d("HolidayListViewModel", "Loading holidays")
        viewModelScope.launch {
            try {
                manageHolidayUseCase.getAllHolidaysStream()
                    .map { holidays -> 
                        Log.d("HolidayListViewModel", "Received ${holidays.size} holidays")
                        holidays.sortedBy { it.startDate }
                    }
                    .collect { sortedHolidays ->
                        Log.d("HolidayListViewModel", "Updating state with ${sortedHolidays.size} holidays")
                        _state.update {
                            it.copy(
                                holidays = sortedHolidays,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("HolidayListViewModel", "Error loading holidays", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load holidays"
                    )
                }
            }
        }
    }

    fun setActiveHoliday(holiday: Holiday) {
        Log.d("HolidayListViewModel", "Setting active holiday: ${holiday.name}")
        viewModelScope.launch {
            try {
                manageHolidayUseCase.setActiveHoliday(holiday.id)
            } catch (e: Exception) {
                Log.e("HolidayListViewModel", "Error setting active holiday", e)
                _state.update {
                    it.copy(error = e.message ?: "Failed to set active holiday")
                }
            }
        }
    }

    fun onEvent(event: HolidayListEvent) {
        when (event) {
            is HolidayListEvent.DeleteHoliday -> {
                Log.d("HolidayListViewModel", "Showing delete confirmation for holiday: ${event.holiday.name}")
                _state.update {
                    it.copy(
                        showDeleteConfirmation = true,
                        holidayToDelete = event.holiday
                    )
                }
            }
            is HolidayListEvent.ConfirmDelete -> {
                Log.d("HolidayListViewModel", "Confirming delete for holiday: ${state.value.holidayToDelete?.name}")
                deleteHoliday()
            }
            is HolidayListEvent.DismissDeleteDialog -> {
                Log.d("HolidayListViewModel", "Dismissing delete dialog")
                _state.update {
                    it.copy(
                        showDeleteConfirmation = false,
                        holidayToDelete = null
                    )
                }
            }
        }
    }

    private fun deleteHoliday() {
        val holidayToDelete = state.value.holidayToDelete ?: return
        Log.d("HolidayListViewModel", "Deleting holiday: ${holidayToDelete.name}")
        
        viewModelScope.launch {
            try {
                manageHolidayUseCase.deleteHoliday(holidayToDelete)
                _state.update {
                    it.copy(
                        showDeleteConfirmation = false,
                        holidayToDelete = null
                    )
                }
            } catch (e: Exception) {
                Log.e("HolidayListViewModel", "Error deleting holiday", e)
                _state.update {
                    it.copy(
                        error = e.message ?: "Failed to delete holiday",
                        showDeleteConfirmation = false,
                        holidayToDelete = null
                    )
                }
            }
        }
    }
} 