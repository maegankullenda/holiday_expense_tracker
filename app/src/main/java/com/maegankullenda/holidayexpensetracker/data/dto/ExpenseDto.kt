package com.maegankullenda.holidayexpensetracker.data.dto

data class ExpenseDto(
    val id: String,
    val holidayId: String,
    val amount: Double,
    val description: String,
    val category: String,
    val date: String,
    val currency: String,
    val createdAt: String,
) 