package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.event

import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.EnteredAmount

sealed class ExchangeRateContainerEvent {

    data class SetCurrencies(val viewPagersState: Pair<String, String>) :
        ExchangeRateContainerEvent()

    data class CalculateEnteredValue(val enteredValueText: EnteredAmount) :
        ExchangeRateContainerEvent()

    object Exchange : ExchangeRateContainerEvent()
}