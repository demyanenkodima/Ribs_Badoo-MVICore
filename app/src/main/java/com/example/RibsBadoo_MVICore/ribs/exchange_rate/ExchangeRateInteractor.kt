package com.example.RibsBadoo_MVICore.ribs.exchange_rate

import androidx.lifecycle.Lifecycle
import com.badoo.binder.Binder
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.common.createDestroyLifecycle
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.IExchangeRateEvent
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.event.IExchangeRateEvent.Companion.exchangeRateEventSource
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature.ExchangeRateFeature
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.mapper.*
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

class ExchangeRateInteractor(
    buildParams: BuildParams<Nothing?>,
    private val exchangeRateFeature: ExchangeRateFeature,
    private val input: ObservableSource<ExchangeRate.Input>,
    private val output: Consumer<ExchangeRate.Output>
) : Interactor<ExchangeRate, ExchangeRateView>(
    buildParams = buildParams
), IExchangeRateEvent {
    override fun onViewCreated(view: ExchangeRateView, viewLifecycle: Lifecycle) {
        Binder(viewLifecycle.createDestroyLifecycle()).apply {
            bind(input to exchangeRateFeature using InputToWish)
            bind(exchangeRateFeature to view using StateToViewModel)
            bind(exchangeRateFeature.news to output using NewsToOutput)
            bind(view to exchangeRateFeature using UiEventToWish)
            bind(exchangeRateEventSource to exchangeRateFeature using ExchangeRateEventToWish)
        }
    }
}