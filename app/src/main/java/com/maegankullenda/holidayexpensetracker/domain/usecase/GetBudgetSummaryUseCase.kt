package com.maegankullenda.holidayexpensetracker.domain.usecase

import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import com.maegankullenda.holidayexpensetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetSummaryUseCase @Inject constructor(
    private val budgetRepository: BudgetRepository
) {
    operator fun invoke(): Flow<BudgetSummary?> {
        return budgetRepository.getBudgetSummary()
    }
} 