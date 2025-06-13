package com.maegankullenda.holidayexpensetracker.domain.repository

import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ExpenseRepository {
    suspend fun getExpenseById(id: String): Expense?
    fun getExpensesStream(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun deleteExpenseById(id: String)
    suspend fun getExpensesByHolidayId(holidayId: String): List<Expense>
    suspend fun getExpensesByDate(holidayId: String, date: LocalDate): List<Expense>
    fun getExpensesStreamByHolidayId(holidayId: String): kotlinx.coroutines.flow.Flow<List<Expense>>
} 