package com.maegankullenda.holidayexpensetracker.domain.usecase

import com.maegankullenda.holidayexpensetracker.domain.model.BudgetCalculator
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetBudgetSummaryUseCase @Inject constructor(
    private val holidayRepository: HolidayRepository,
    private val expenseRepository: ExpenseRepository,
    private val budgetCalculator: BudgetCalculator,
) {
    operator fun invoke(): Flow<BudgetSummary?> {
        return combine(
            holidayRepository.getCurrentHolidayStream(),
            expenseRepository.getExpensesStreamByHolidayId("current"),
        ) { holiday, expenses ->
            holiday?.let { currentHoliday ->
                budgetCalculator.calculateBudgetSummary(
                    holiday = currentHoliday,
                    expenses = expenses,
                )
            }
        }
    }
} 