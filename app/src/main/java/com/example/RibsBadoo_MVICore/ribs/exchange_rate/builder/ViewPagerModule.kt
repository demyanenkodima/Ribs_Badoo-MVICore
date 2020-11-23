package com.example.RibsBadoo_MVICore.ribs.exchange_rate.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRateInteractor
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.feature.ExchangeRateFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object ViewPagerModule {

    @ViewPagerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        exchangeRateFeature: ExchangeRateFeature,
        buildParams: BuildParams<Nothing?>,
        input: ObservableSource<ExchangeRate.Input>,
        output: Consumer<ExchangeRate.Output>
    ): ExchangeRateInteractor =
        ExchangeRateInteractor(
            buildParams = buildParams,
            input = input,
            output = output,
            exchangeRateFeature = exchangeRateFeature
        )

    @ViewPagerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: ExchangeRate.Customisation,
        interactor: ExchangeRateInteractor
    ): Node<ExchangeRateView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor)
    )

    @ViewPagerScope
    @Provides
    @JvmStatic
    internal fun provideViewPagerFeature() = ExchangeRateFeature()
}
