package com.maegankullenda.holidayexpensetracker.di

import android.content.Context
import androidx.room.Room
import com.maegankullenda.holidayexpensetracker.data.local.HolidayDatabase
import com.maegankullenda.holidayexpensetracker.data.local.dao.ExpenseDao
import com.maegankullenda.holidayexpensetracker.data.local.dao.HolidayDao
import com.maegankullenda.holidayexpensetracker.data.repository.impl.ExpenseRepositoryImpl
import com.maegankullenda.holidayexpensetracker.data.repository.HolidayRepositoryImpl
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHolidayDatabase(
        @ApplicationContext context: Context,
    ): HolidayDatabase {
        return Room.databaseBuilder(
            context,
            HolidayDatabase::class.java,
            "holiday_database",
        ).build()
    }

    @Provides
    fun provideHolidayDao(database: HolidayDatabase): HolidayDao {
        return database.holidayDao()
    }

    @Provides
    fun provideExpenseDao(database: HolidayDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideHolidayRepository(holidayDao: HolidayDao): HolidayRepository {
        return HolidayRepositoryImpl(holidayDao)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }
} 