package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container

import androidx.lifecycle.Lifecycle
import com.badoo.binder.Binder
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.common.createDestroyLifecycle
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.event.ExchangeRateContainerEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature.GlobalStateFeature
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper.*
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper.GlobalStateNewsToAppBarInput
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper.GlobalStateNewsToExchangeRateInput
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper.InputToWish
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.mapper.NewsToOutput
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class ExchangeRateContainerInteractor(
    buildParams: BuildParams<*>,
    private val globalStateFeature: GlobalStateFeature,
    private val input: ObservableSource<ExchangeRateContainer.Input>,
    private val output: Consumer<ExchangeRateContainer.Output>
) : Interactor<ExchangeRateContainer, ExchangeRateContainerView>(
    buildParams = buildParams
), IExchangeRateContainerEvent {

    internal val exchangeRateInputSource: Relay<ExchangeRate.Input> = BehaviorRelay.create()
    internal val appBarInputSource: Relay<AppBar.Input> = BehaviorRelay.create()
    override var exchangeRateContainerEventSource: Relay<ExchangeRateContainerEvent> =
        PublishRelay.create()

    override fun onAttach(nodeLifecycle: Lifecycle) {
        Binder(nodeLifecycle.createDestroyLifecycle()).apply {
            bind(input to globalStateFeature using InputToWish)
            bind(globalStateFeature.news to exchangeRateInputSource using GlobalStateNewsToExchangeRateInput)
            bind(globalStateFeature.news to output using NewsToOutput)
            bind(globalStateFeature.news to appBarInputSource using GlobalStateNewsToAppBarInput)
            bind(exchangeRateContainerEventSource to globalStateFeature using EventToWish())
        }
    }

    internal val exchangeRateOutputConsumer: Consumer<ExchangeRate.Output> = Consumer {
        when (it) {
            is ExchangeRate.Output.SetViewPagerState -> {
                accept(ExchangeRateContainerEvent.SetCurrencies(it.viewPagersState))
            }
            is ExchangeRate.Output.EnteredValue -> {
                accept(ExchangeRateContainerEvent.CalculateEnteredValue(it.enteredValueText))
            }
        }
    }

    internal val appBarOutputConsumer: Consumer<AppBar.Output> = Consumer {
        when (it) {
            is AppBar.Output.Exchange -> accept(ExchangeRateContainerEvent.Exchange)
        }
    }
}

interface IExchangeRateContainerEvent {
    var exchangeRateContainerEventSource: Relay<ExchangeRateContainerEvent>
    fun accept(exchangeRateContainerEvent: ExchangeRateContainerEvent) {
        exchangeRateContainerEventSource.accept(exchangeRateContainerEvent)
    }
}
