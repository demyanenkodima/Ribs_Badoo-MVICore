package com.example.RibsBadoo_MVICore.common.room

import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import java.math.BigDecimal

fun getSupportedCurrencies() = listOf("EUR", "GBP", "USD", "RUB")

fun createDefaultWallets(): List<Wallet> {
    val defaultBalance = BigDecimal(100)
    val walletList = mutableListOf<Wallet>()

    getSupportedCurrencies().forEachIndexed { index, currency ->
        walletList.add(
            Wallet(currency = currency, balance = defaultBalance)
        )
    }
    return walletList
}