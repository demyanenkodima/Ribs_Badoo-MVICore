package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper

import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.event.ExchangeRateContainerEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature.GlobalStateFeature

class EventToWish :
        (ExchangeRateContainerEvent) -> GlobalStateFeature.Wish? {
    override fun invoke(event: ExchangeRateContainerEvent): GlobalStateFeature.Wish? =
        when (event) {
            is ExchangeRateContainerEvent.SetCurrencies -> GlobalStateFeature.Wish.SetCurrentCurrencies(
                event.viewPagersState
            )
            is ExchangeRateContainerEvent.Exchange -> GlobalStateFeature.Wish.Exchange
            is ExchangeRateContainerEvent.CalculateEnteredValue -> GlobalStateFeature.Wish.CalculateEnteredValue(
                event.enteredValueText.currency,
                event.enteredValueText.fragmentCurrencyType
            )
        }
}
