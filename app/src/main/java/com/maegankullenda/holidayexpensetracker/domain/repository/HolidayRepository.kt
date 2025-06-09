package com.maegankullenda.holidayexpensetracker.domain.repository

import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import kotlinx.coroutines.flow.Flow

interface HolidayRepository {
    suspend fun createHoliday(holiday: Holiday)
    suspend fun updateHoliday(holiday: Holiday)
    suspend fun deleteHoliday(holiday: Holiday)
    suspend fun getHolidayById(id: String): Holiday?
    fun getCurrentHolidayStream(): Flow<Holiday?>
    fun getAllHolidaysStream(): Flow<List<Holiday>>
    suspend fun setActiveHoliday(holidayId: String)
} 