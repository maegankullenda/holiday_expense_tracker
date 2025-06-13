package com.maegankullenda.holidayexpensetracker.data.repository.impl

import com.maegankullenda.holidayexpensetracker.data.local.dao.ExpenseDao
import com.maegankullenda.holidayexpensetracker.data.local.entity.ExpenseEntity
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao,
) : ExpenseRepository {

    override suspend fun getExpenseById(id: String): Expense? {
        return expenseDao.getExpenseById(id)?.toDomain()
    }

    override fun getExpensesStream(): Flow<List<Expense>> {
        return expenseDao.getExpensesStream().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getExpensesByHolidayId(holidayId: String): List<Expense> {
        return expenseDao.getExpensesByHolidayId(holidayId).map { it.toDomain() }
    }

    override suspend fun getExpensesByDate(holidayId: String, date: LocalDate): List<Expense> {
        return expenseDao.getExpensesByDate(holidayId, date).map { it.toDomain() }
    }

    override suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense.toEntity())
    }

    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }

    override suspend fun deleteExpenseById(id: String) {
        expenseDao.deleteExpenseById(id)
    }

    override fun getExpensesStreamByHolidayId(holidayId: String): Flow<List<Expense>> {
        return expenseDao.getExpensesStreamByHolidayId(holidayId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun ExpenseEntity.toDomain(): Expense {
        return Expense(
            id = id,
            holidayId = holidayId,
            amount = amount,
            description = description,
            category = category,
            date = date.toLocalDate(),
            currency = currency,
            createdAt = createdAt
        )
    }

    private fun Expense.toEntity(): ExpenseEntity {
        return ExpenseEntity(
            id = id,
            holidayId = holidayId,
            amount = amount,
            description = description,
            category = category,
            date = date.atStartOfDay(),
            currency = currency,
            createdAt = createdAt
        )
    }
} 