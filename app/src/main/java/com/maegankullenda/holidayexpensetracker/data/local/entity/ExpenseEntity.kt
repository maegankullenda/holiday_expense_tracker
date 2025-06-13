package com.maegankullenda.holidayexpensetracker.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import java.time.LocalDateTime

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = HolidayEntity::class,
            parentColumns = ["id"],
            childColumns = ["holidayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("holidayId")]
)
data class ExpenseEntity(
    @PrimaryKey
    val id: String,
    val holidayId: String,
    val amount: Double,
    val description: String,
    val category: ExpenseCategory,
    val date: LocalDateTime,
    val currency: Currency,
    val createdAt: LocalDateTime = LocalDateTime.now()
) 