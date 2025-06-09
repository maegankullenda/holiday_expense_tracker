package com.maegankullenda.holidayexpensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import java.time.LocalDate

@Entity(tableName = "holidays")
data class HolidayEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val destination: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val totalBudget: Double,
    val currency: Currency,
    val isActive: Boolean = false,
) 