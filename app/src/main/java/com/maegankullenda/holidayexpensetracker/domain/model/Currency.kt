package com.maegankullenda.holidayexpensetracker.domain.model

enum class Currency(val symbol: String, val code: String) {
    ZAR("R", "ZAR"),
    USD("$", "USD"),
    EUR("€", "EUR"),
    GBP("£", "GBP"),
    AUD("A$", "AUD"),
    CAD("C$", "CAD");

    override fun toString(): String {
        return code
    }
} 