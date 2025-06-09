package com.maegankullenda.holidayexpensetracker.data.local.dao

import androidx.room.*
import com.maegankullenda.holidayexpensetracker.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: String)

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: String): ExpenseEntity?

    @Query("SELECT * FROM expenses WHERE holidayId = :holidayId ORDER BY date DESC")
    suspend fun getExpensesByHolidayId(holidayId: String): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE holidayId = :holidayId AND date = :date")
    suspend fun getExpensesByDate(holidayId: String, date: LocalDate): List<ExpenseEntity>

    @Query("SELECT * FROM expenses WHERE holidayId = :holidayId ORDER BY date DESC, createdAt DESC")
    fun getExpensesStreamByHolidayId(holidayId: String): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expenses WHERE holidayId = :holidayId ORDER BY date DESC, createdAt DESC")
    fun getAllExpensesStream(holidayId: String): Flow<List<ExpenseEntity>>
} 