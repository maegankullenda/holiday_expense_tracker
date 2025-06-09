package com.maegankullenda.holidayexpensetracker.data.mapper

import com.maegankullenda.holidayexpensetracker.data.dto.ExpenseDto
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.Expense
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

class ExpenseMapper @Inject constructor() {
    fun mapToDomain(dto: ExpenseDto): Expense {
        return Expense(
            id = dto.id,
            amount = dto.amount,
            description = dto.description,
            category = ExpenseCategory.valueOf(dto.category),
            date = LocalDate.parse(dto.date),
            currency = Currency.valueOf(dto.currency),
            holidayId = dto.holidayId,
            createdAt = LocalDateTime.parse(dto.createdAt),
        )
    }

    fun mapToDto(domain: Expense): ExpenseDto {
        return ExpenseDto(
            id = domain.id ?: "",
            amount = domain.amount,
            description = domain.description,
            category = domain.category.name,
            date = domain.date.toString(),
            currency = domain.currency.name,
            holidayId = domain.holidayId,
            createdAt = domain.createdAt.toString(),
        )
    }
} 