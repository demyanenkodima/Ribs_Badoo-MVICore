package com.example.RibsBadoo_MVICore.common

import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import java.math.BigDecimal

fun getCurrencySymbol(currency: String) = when (currency) {
    "EUR" -> "\u20ac"
    "GBP" -> "\u00a3"
    "USD" -> "\u0024"
    "RUB" -> "\u20bd"
    else -> throw Exception("Unknown currency. Symbol not found.")
}

fun getMoneyPrefixToView(): BigDecimal = BigDecimal.ONE

fun getExchangeRateToIne(
    fragmentCurrencyType: FragmentCurrencyType,
    exchangeRate: CalculatedExchangeRate
): String {
    return when (fragmentCurrencyType) {
        FragmentCurrencyType.SALE -> {
            "${getMoneyPrefixToView()}" +
                    "${getCurrencySymbol(exchangeRate.currencySale)}=" +
                    "${exchangeRate.rateSale}" +
                    getCurrencySymbol(exchangeRate.currencyPurchase)
        }

        FragmentCurrencyType.PURCHASE -> {
            "${getMoneyPrefixToView()}" +
                    "${getCurrencySymbol(exchangeRate.currencyPurchase)}=" +
                    "${exchangeRate.ratePurchase}" +
                    getCurrencySymbol(exchangeRate.currencySale)
        }
    }
}