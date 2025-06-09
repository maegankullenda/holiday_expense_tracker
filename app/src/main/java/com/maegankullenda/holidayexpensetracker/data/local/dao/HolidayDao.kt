package com.maegankullenda.holidayexpensetracker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.maegankullenda.holidayexpensetracker.data.local.entity.HolidayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HolidayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoliday(holiday: HolidayEntity)

    @Update
    suspend fun updateHoliday(holiday: HolidayEntity)

    @Delete
    suspend fun deleteHoliday(holiday: HolidayEntity)

    @Query("SELECT * FROM holidays WHERE id = :id")
    suspend fun getHolidayById(id: String): HolidayEntity?

    @Query("SELECT * FROM holidays WHERE isActive = 1 LIMIT 1")
    fun getCurrentHolidayStream(): Flow<HolidayEntity?>

    @Query("SELECT * FROM holidays ORDER BY startDate DESC")
    fun getAllHolidaysStream(): Flow<List<HolidayEntity>>

    @Query("UPDATE holidays SET isActive = 0")
    suspend fun setAllHolidaysInactive()

    @Query("UPDATE holidays SET isActive = :isActive WHERE id = :holidayId")
    suspend fun setHolidayActive(holidayId: String, isActive: Boolean)
} 