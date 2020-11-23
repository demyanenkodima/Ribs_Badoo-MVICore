package com.example.RibsBadoo_MVICore.ribs.root.event

import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import java.math.BigDecimal

sealed class RootEvent {
    data class CalculateExchangeRateToOne(
        val currencySale: String,
        val currencyPurchase: String,
        val money: BigDecimal,
        val currencyExchangeRateList: List<CurrencyExchangeRate>
    ) : RootEvent()

    data class SendCalculateEnteredValue(
        val currencySale: String,
        val currencyPurchase: String,
        val money: BigDecimal,
        val currencyExchangeRateList: List<CurrencyExchangeRate>,
        val fragmentCurrencyType: FragmentCurrencyType
    ) : RootEvent()

    object GetWallet : RootEvent()
//    data class CalculateEnteredValue(val enteredValueText: CurrencyFragmentNew.EnteredValueText) :
//        RootEvent()

    data class Exchange(val calculatedEnteredValue: CalculatedEnteredValue) : RootEvent()
}