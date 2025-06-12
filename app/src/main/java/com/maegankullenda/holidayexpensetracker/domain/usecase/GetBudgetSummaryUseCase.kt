package com.maegankullenda.holidayexpensetracker.domain.usecase

import com.maegankullenda.holidayexpensetracker.domain.model.BudgetCalculator
import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.repository.ExpenseRepository
import com.maegankullenda.holidayexpensetracker.domain.repository.HolidayRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBudgetSummaryUseCase @Inject constructor(
    private val holidayRepository: HolidayRepository,
    private val expenseRepository: ExpenseRepository,
    private val budgetCalculator: BudgetCalculator,
) {
    operator fun invoke(): Flow<BudgetSummary?> {
        return holidayRepository.getCurrentHolidayStream().flatMapLatest { holiday ->
            if (holiday == null) {
                flowOf(null)
            } else {
                expenseRepository.getExpensesStreamByHolidayId(holiday.id)
                    .map { expenses ->
                        budgetCalculator.calculateBudgetSummary(
                            holiday = holiday,
                            expenses = expenses,
                        )
                    }
            }
        }
    }
} 