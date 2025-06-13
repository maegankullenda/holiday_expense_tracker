package com.maegankullenda.holidayexpensetracker.di

import android.content.Context
import androidx.room.Room
import com.maegankullenda.holidayexpensetracker.data.local.HolidayDatabase
import com.maegankullenda.holidayexpensetracker.data.local.dao.ExpenseDao
import com.maegankullenda.holidayexpensetracker.data.local.dao.HolidayDao
import com.maegankullenda.holidayexpensetracker.data.repository.HolidayRepositoryImpl
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import com.maegankullenda.holidayexpensetracker.data.repository.impl.ExpenseRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHolidayDatabase(
        @ApplicationContext context: Context
    ): HolidayDatabase {
        return Room.databaseBuilder(
            context,
            HolidayDatabase::class.java,
            "holiday_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHolidayDao(db: HolidayDatabase): HolidayDao {
        return db.holidayDao()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(db: HolidayDatabase): ExpenseDao {
        return db.expenseDao()
    }

    @Provides
    @Singleton
    fun provideHolidayRepository(
        holidayDao: HolidayDao
    ): HolidayRepository {
        return HolidayRepositoryImpl(holidayDao)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        expenseDao: ExpenseDao
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }

    @Provides
    @Singleton
    fun provideBudgetRepository(
        holidayRepository: HolidayRepository,
        expenseRepository: ExpenseRepository
    ): com.maegankullenda.holidayexpensetracker.domain.repository.BudgetRepository {
        return com.maegankullenda.holidayexpensetracker.data.repository.BudgetRepositoryImpl(
            holidayRepository,
            expenseRepository
        )
    }
} 