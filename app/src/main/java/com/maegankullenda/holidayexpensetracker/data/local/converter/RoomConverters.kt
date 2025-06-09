package com.maegankullenda.holidayexpensetracker.data.local.converter

import androidx.room.TypeConverter
import com.maegankullenda.holidayexpensetracker.domain.model.Currency
import com.maegankullenda.holidayexpensetracker.domain.model.ExpenseCategory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RoomConverters {
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, dateFormatter) }
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(dateTimeFormatter)
    }

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }

    @TypeConverter
    fun fromCurrency(currency: Currency): String {
        return currency.name
    }

    @TypeConverter
    fun toCurrency(currencyName: String): Currency {
        return Currency.valueOf(currencyName)
    }

    @TypeConverter
    fun fromExpenseCategory(category: ExpenseCategory): String {
        return category.name
    }

    @TypeConverter
    fun toExpenseCategory(categoryName: String): ExpenseCategory {
        return ExpenseCategory.valueOf(categoryName)
    }
} 