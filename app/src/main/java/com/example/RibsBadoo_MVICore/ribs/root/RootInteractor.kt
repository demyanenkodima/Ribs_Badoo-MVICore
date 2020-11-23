package com.example.RibsBadoo_MVICore.ribs.root

import androidx.lifecycle.Lifecycle
import com.badoo.binder.Binder
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.BackStackInteractor
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.common.createDestroyLifecycle
import com.example.RibsBadoo_MVICore.feature.CalculatorFeature
import com.example.RibsBadoo_MVICore.feature.ExchangeRateProviderFeature
import com.example.RibsBadoo_MVICore.feature.WalletFeature
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.root.event.RootEvent
import com.example.RibsBadoo_MVICore.ribs.root.mapper.*
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.functions.Consumer

class RootInteractor(
    private val exchangeRateProviderFeature: ExchangeRateProviderFeature,
    private val walletFeature: WalletFeature,
    private val calculatorFeature: CalculatorFeature,
    buildParams: BuildParams<Nothing?>,
) : BackStackInteractor<Root, Nothing, RootRouter.Configuration>(
    buildParams = buildParams,
    initialConfiguration = RootRouter.Configuration.Permanent.ExchangeRateContainer
), IRootEvent {

    internal val exchangeRateContainerInputSource: Relay<ExchangeRateContainer.Input> =
        BehaviorRelay.create()
    override var rootEventSource: Relay<RootEvent> = PublishRelay.create()

    override fun onAttach(nodeLifecycle: Lifecycle) {
        super.onAttach(nodeLifecycle)
        Binder(nodeLifecycle.createDestroyLifecycle()).apply {
            bind(exchangeRateProviderFeature.news to exchangeRateContainerInputSource using ExchangeRateProvideFeatureNewsToExchangeRateContainerInput)
            bind(rootEventSource to calculatorFeature using RootEventToCalculatorFeatureWish())
            bind(calculatorFeature.news to exchangeRateContainerInputSource using CalculatorFeatureNewsToExchangeRateContainerInput)
            bind(rootEventSource to walletFeature using RootEventToWalletFeatureWish())
            bind(walletFeature.news to exchangeRateContainerInputSource using WalletFeatureNewsToExchangeRateContainerInput)
        }
    }

    internal val exchangeRateContainerOutputConsumer: Consumer<ExchangeRateContainer.Output> =
        Consumer {
            when (it) {
                is ExchangeRateContainer.Output.CalculateExchangeRateToOne -> {
                    accept(RootEvent.GetWallet)
                    accept(
                        RootEvent.CalculateExchangeRateToOne(
                            currencySale = it.currencySale,
                            currencyPurchase = it.currencyPurchase,
                            money = it.money,
                            currencyExchangeRateList = it.currencyExchangeRateList
                        )
                    )
                }
                is ExchangeRateContainer.Output.CalculateEnteredValue -> {
                    accept(
                        RootEvent.SendCalculateEnteredValue(
                            currencySale = it.currencySale,
                            currencyPurchase = it.currencyPurchase,
                            money = it.money,
                            currencyExchangeRateList = it.currencyExchangeRateList,
                            fragmentCurrencyType = it.fragmentCurrencyType
                        )
                    )
                }
                is ExchangeRateContainer.Output.Exchange -> {
                    accept(RootEvent.Exchange(calculatedEnteredValue = it.calculatedEnteredValue))
                }
            }
        }
}

interface IRootEvent {
    var rootEventSource: Relay<RootEvent>
    fun accept(rootEvent: RootEvent) {
        rootEventSource.accept(rootEvent)
    }
}