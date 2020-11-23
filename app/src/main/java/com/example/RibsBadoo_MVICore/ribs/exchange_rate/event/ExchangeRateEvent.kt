package com.example.RibsBadoo_MVICore.ribs.exchange_rate.event

import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.EnteredAmount
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay

sealed class ExchangeRateEvent {
    data class WalletSet(val walletList: List<Wallet>?) : ExchangeRateEvent()
    data class ExchangeRateToOne(val exchangeRateToOne: CalculatedExchangeRate?) :
        ExchangeRateEvent()

    data class CalculatedEnteredMoney(val calculatedEnteredValue: CalculatedEnteredValue?) :
        ExchangeRateEvent()

    data class SetEnteredAmount(val enteredAmount: EnteredAmount) : ExchangeRateEvent()
    data class SetIsViewPagersSwipeable(val isSwipeable: Boolean) : ExchangeRateEvent()
    object ClearEditText : ExchangeRateEvent()
}

interface IExchangeRateEvent {
    companion object {
        var exchangeRateEventSource: Relay<ExchangeRateEvent> = PublishRelay.create()
    }

    fun sendEvent(exchangeRateEvent: ExchangeRateEvent) {
        exchangeRateEventSource.accept(exchangeRateEvent)
    }
}