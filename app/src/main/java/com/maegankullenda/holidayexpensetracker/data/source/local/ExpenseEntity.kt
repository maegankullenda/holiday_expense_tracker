package com.maegankullenda.holidayexpensetracker.data.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val amount: Double,
    val description: String,
    val category: String,
    val date: Long,
    val location: String?,
    val currency: String
) 