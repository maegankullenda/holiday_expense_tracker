package com.maegankullenda.holidayexpensetracker.domain.usecase

import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageHolidayUseCase @Inject constructor(
    private val holidayRepository: HolidayRepository,
) {
    suspend fun createHoliday(holiday: Holiday) {
        holidayRepository.createHoliday(holiday)
    }

    suspend fun updateHoliday(holiday: Holiday) {
        holidayRepository.updateHoliday(holiday)
    }

    suspend fun deleteHoliday(holiday: Holiday) {
        holidayRepository.deleteHoliday(holiday)
    }

    suspend fun getHolidayById(id: String): Holiday? {
        return holidayRepository.getHolidayById(id)
    }

    fun getCurrentHolidayStream(): Flow<Holiday?> {
        return holidayRepository.getCurrentHolidayStream()
    }

    fun getAllHolidaysStream(): Flow<List<Holiday>> {
        return holidayRepository.getAllHolidaysStream()
    }

    suspend fun setActiveHoliday(holidayId: String) {
        holidayRepository.setActiveHoliday(holidayId)
    }
} 