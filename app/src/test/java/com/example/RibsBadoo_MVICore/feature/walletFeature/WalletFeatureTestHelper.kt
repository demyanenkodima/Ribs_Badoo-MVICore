package com.example.RibsBadoo_MVICore.feature.walletFeature

import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import org.mockito.Mockito
import java.math.BigDecimal

class WalletFeatureTestHelper {
    companion object {
        private var defaultMoney = BigDecimal(100)
        private var currencySale = "EUR"
        private var currencyPurchase = "RUB"
        private var rateSale = BigDecimal(90.26)
        private var ratePurchase = BigDecimal(0.01)
        private var amountForSale = BigDecimal.ONE
        private var amountForPurchase = BigDecimal(90.26)

        var walletList = listOf(
            Wallet(currency = "EUR", balance = defaultMoney),
            Wallet(currency = "GBP", balance = defaultMoney),
            Wallet(currency = "RUB", balance = defaultMoney),
            Wallet(currency = "USD", balance = defaultMoney)
        )

        private val calculatedExchangeRate = CalculatedExchangeRate(
            currencySale = currencySale,
            currencyPurchase = currencyPurchase,
            rateSale = rateSale,
            ratePurchase = ratePurchase
        )

        val enteredAmount = CalculatedEnteredValue(
            calculatedExchangeRate = calculatedExchangeRate,
            amountForSale = amountForSale,
            amountForPurchase = amountForPurchase,
        )

        var walletListAfterExchange = listOf(
            Wallet(currency = "EUR", balance = defaultMoney.minus(amountForSale)),
            Wallet(currency = "GBP", balance = defaultMoney),
            Wallet(currency = "RUB", balance = defaultMoney.plus(amountForPurchase)),
            Wallet(currency = "USD", balance = defaultMoney)
        )
    }
}

fun <T> any(): T = Mockito.any<T>()