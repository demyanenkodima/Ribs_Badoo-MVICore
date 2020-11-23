package com.example.RibsBadoo_MVICore.ribs.root.mapper

import com.example.RibsBadoo_MVICore.feature.CalculatorFeature
import com.example.RibsBadoo_MVICore.feature.WalletFeature
import com.example.RibsBadoo_MVICore.ribs.root.event.RootEvent

class RootEventToCalculatorFeatureWish : (RootEvent) -> CalculatorFeature.Wish? {
    override fun invoke(event: RootEvent): CalculatorFeature.Wish? =
        when (event) {
            is RootEvent.CalculateExchangeRateToOne -> CalculatorFeature.Wish.CalculateExchangeRateToOne(
                currencySale = event.currencySale,
                currencyPurchase = event.currencyPurchase,
                money = event.money,
                currencyExchangeRateList = event.currencyExchangeRateList
            )
            is RootEvent.SendCalculateEnteredValue -> CalculatorFeature.Wish.CalculateEnteredValue(
                currencySale = event.currencySale,
                currencyPurchase = event.currencyPurchase,
                money = event.money,
                currencyExchangeRateList = event.currencyExchangeRateList,
                fragmentCurrencyType = event.fragmentCurrencyType
            )
            else -> null
        }
}

class RootEventToWalletFeatureWish : (RootEvent) -> WalletFeature.Wish? {
    override fun invoke(event: RootEvent): WalletFeature.Wish? =
        when (event) {
            is RootEvent.CalculateExchangeRateToOne -> WalletFeature.Wish.LoadWallet
            is RootEvent.Exchange -> WalletFeature.Wish.Exchange(event.calculatedEnteredValue)
            else -> null
        }
}
