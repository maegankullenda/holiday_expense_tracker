package com.maegankullenda.holidayexpensetracker.domain.usecase

import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class ManageExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) {
    suspend fun addExpense(expense: Expense) {
        expenseRepository.addExpense(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseRepository.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseRepository.deleteExpense(expense)
    }

    suspend fun getExpenseById(id: String): Expense? {
        return expenseRepository.getExpenseById(id)
    }
} 