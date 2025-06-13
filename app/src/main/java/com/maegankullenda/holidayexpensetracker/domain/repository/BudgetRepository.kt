package com.maegankullenda.holidayexpensetracker.domain.repository

import com.maegankullenda.holidayexpensetracker.domain.model.BudgetSummary
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getBudgetSummary(): Flow<BudgetSummary?>
} 