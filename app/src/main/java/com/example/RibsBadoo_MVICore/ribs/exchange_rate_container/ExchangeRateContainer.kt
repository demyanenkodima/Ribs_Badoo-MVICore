package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.FragmentCurrencyType
import com.example.RibsBadoo_MVICore.common.retrofit.model.CurrencyExchangeRate
import com.example.RibsBadoo_MVICore.common.retrofit.model.ExchangeRateResponse
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import com.example.RibsBadoo_MVICore.feature.CalculatedExchangeRate
import com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer.Input
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer.Output
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer
import java.math.BigDecimal

interface ExchangeRateContainer : Rib, Connectable<Input, Output> {

    interface Dependency {
        fun exchangeRateContainerInput(): ObservableSource<Input>
        fun exchangeRateContainerOutput(): Consumer<Output>
    }

    sealed class Input {
        data class ResponseFromApi(val exchangeRateResponse: ExchangeRateResponse) : Input()
        data class ExchangeRateToOne(val calculatedExchangeRate: CalculatedExchangeRate) : Input()
        data class WalletSet(val walletList: List<Wallet>) : Input()
        data class CalculatedEnteredValue(val calculatedEnteredValue: com.example.RibsBadoo_MVICore.feature.CalculatedEnteredValue) : Input()
        data class ExchangeError(val throwable: Throwable) : Input()
    }

    sealed class Output {
        data class CalculateExchangeRateToOne(
            val currencySale: String,
            val currencyPurchase: String,
            val money: BigDecimal,
            val currencyExchangeRateList: List<CurrencyExchangeRate>
        ) : Output()

        data class CalculateEnteredValue(
            val currencySale: String,
            val currencyPurchase: String,
            val money: BigDecimal,
            val currencyExchangeRateList: List<CurrencyExchangeRate>,
            val fragmentCurrencyType: FragmentCurrencyType
        ) : Output()

        data class Exchange(val calculatedEnteredValue: CalculatedEnteredValue) : Output()
    }

    class Customisation(
        val viewFactory: ExchangeRateContainerView.Factory = ExchangeRateContainerViewImpl.Factory(),
    )
}
