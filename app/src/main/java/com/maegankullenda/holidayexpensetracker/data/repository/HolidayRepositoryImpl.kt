package com.maegankullenda.holidayexpensetracker.data.repository

import com.maegankullenda.holidayexpensetracker.data.local.dao.HolidayDao
import com.maegankullenda.holidayexpensetracker.data.local.entity.HolidayEntity
import com.maegankullenda.holidayexpensetracker.domain.model.Holiday
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class HolidayRepositoryImpl @Inject constructor(
    private val holidayDao: HolidayDao,
) : HolidayRepository {

    override suspend fun createHoliday(holiday: Holiday) {
        holidayDao.insertHoliday(holiday.toEntity())
    }

    override suspend fun updateHoliday(holiday: Holiday) {
        holidayDao.updateHoliday(holiday.toEntity())
    }

    override suspend fun deleteHoliday(holiday: Holiday) {
        holidayDao.deleteHoliday(holiday.toEntity())
    }

    override suspend fun getHolidayById(id: String): Holiday? {
        return holidayDao.getHolidayById(id)?.toDomain()
    }

    override fun getCurrentHolidayStream(): Flow<Holiday?> {
        return holidayDao.getCurrentHolidayStream().map { it?.toDomain() }
    }

    override fun getAllHolidaysStream(): Flow<List<Holiday>> {
        return holidayDao.getAllHolidaysStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun setActiveHoliday(holidayId: String) {
        // First, set all holidays to inactive
        holidayDao.setAllHolidaysInactive()
        // Then, set the selected holiday to active
        holidayDao.setHolidayActive(holidayId, true)
    }

    private fun Holiday.toEntity(): HolidayEntity {
        return HolidayEntity(
            id = id ?: UUID.randomUUID().toString(),
            name = name,
            destination = destination,
            startDate = startDate,
            endDate = endDate,
            totalBudget = totalBudget,
            currency = currency,
            isActive = isActive,
        )
    }

    private fun HolidayEntity.toDomain(): Holiday {
        return Holiday(
            id = id,
            name = name,
            destination = destination,
            startDate = startDate,
            endDate = endDate,
            totalBudget = totalBudget,
            currency = currency,
            isActive = isActive,
        )
    }
} 