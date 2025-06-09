package com.maegankullenda.holidayexpensetracker.presentation.ui.holiday

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
        loadHolidays()
    }

    private fun loadHolidays() {
        viewModelScope.launch {
            try {
                manageHolidayUseCase.getAllHolidaysStream()
                    .map { holidays -> 
                        holidays.sortedBy { it.startDate }
                    }
                    .collect { sortedHolidays ->
                        _state.update {
                            it.copy(
                                holidays = sortedHolidays,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
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
        viewModelScope.launch {
            try {
                manageHolidayUseCase.setActiveHoliday(holiday.id)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.message ?: "Failed to set active holiday")
                }
            }
        }
    }

    fun onEvent(event: HolidayListEvent) {
        when (event) {
            is HolidayListEvent.DeleteHoliday -> {
                _state.update {
                    it.copy(
                        showDeleteConfirmation = true,
                        holidayToDelete = event.holiday
                    )
                }
            }
            is HolidayListEvent.ConfirmDelete -> {
                deleteHoliday()
            }
            is HolidayListEvent.DismissDeleteDialog -> {
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