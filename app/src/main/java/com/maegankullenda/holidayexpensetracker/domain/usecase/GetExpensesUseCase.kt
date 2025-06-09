package com.maegankullenda.holidayexpensetracker.domain.usecase

import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
) {
    operator fun invoke(holidayId: String): Flow<List<Expense>> {
        return expenseRepository.getExpensesStreamByHolidayId(holidayId)
    }
} 