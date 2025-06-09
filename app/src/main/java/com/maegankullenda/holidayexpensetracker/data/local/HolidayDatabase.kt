package com.maegankullenda.holidayexpensetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maegankullenda.holidayexpensetracker.data.local.converter.RoomConverters
import com.maegankullenda.holidayexpensetracker.data.local.dao.ExpenseDao
import com.maegankullenda.holidayexpensetracker.data.local.dao.HolidayDao
import com.maegankullenda.holidayexpensetracker.data.local.entity.ExpenseEntity
import com.maegankullenda.holidayexpensetracker.data.local.entity.HolidayEntity

@Database(
    entities = [
        HolidayEntity::class,
        ExpenseEntity::class,
    ],
    version = 1,
)
@TypeConverters(RoomConverters::class)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidayDao(): HolidayDao
    abstract fun expenseDao(): ExpenseDao
} 