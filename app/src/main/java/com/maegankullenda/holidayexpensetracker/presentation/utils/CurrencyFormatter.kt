package com.maegankullenda.holidayexpensetracker.presentation.utils

import com.maegankullenda.holidayexpensetracker.domain.model.Currency

fun formatCurrency(amount: Double, currency: Currency): String {
    return when (currency) {
        Currency.ZAR -> "R%.2f".format(amount)
        Currency.USD -> "$%.2f".format(amount)
        Currency.EUR -> "€%.2f".format(amount)
        Currency.GBP -> "£%.2f".format(amount)
        Currency.AUD -> "A$%.2f".format(amount)
        Currency.CAD -> "C$%.2f".format(amount)
    }
} 