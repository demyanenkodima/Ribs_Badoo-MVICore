package com.example.RibsBadoo_MVICore.ribs.exchange_rate

import com.badoo.ribs.core.Rib
import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.EnteredAmount
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateViewImpl
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface ExchangeRate : Rib {
    interface Dependency {
        fun exchangeRateInput(): ObservableSource<Input>
        fun exchangeRateOutput(): Consumer<Output>
    }

    sealed class Input {
        data class Fragments(val currencyExchangeRateList: List<CurrencyExchangeRate>) : Input()
        data class ExchangeRateToOne(val exchangeRateToOne: CalculatedExchangeRate) : Input()
        data class WalletSet(val walletList: List<Wallet>) : Input()
        data class CalculatedEnteredValue(val calculatedEnteredValueEnteredValue: com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue) :
            Input()
        data class ExchangeError(val throwable: Throwable) : Input()
    }

    sealed class Output {
        data class SetViewPagerState(val viewPagersState: Pair<String, String>) : Output()
        data class EnteredValue(val enteredValueText: EnteredAmount) :
            Output()
    }

    class Customisation(
        val viewFactory: ExchangeRateView.Factory = ExchangeRateViewImpl.Factory(),
    )
}


