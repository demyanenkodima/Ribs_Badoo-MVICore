package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.app_bar.builder.AppBarBuilder
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.builder.ViewPagerBuilder
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerInteractor
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerRouter
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerView
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.feature.GlobalStateFeature
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object ExchangeRateContainerModule {

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        globalStateFeature: GlobalStateFeature,
        input: ObservableSource<ExchangeRateContainer.Input>,
        output: Consumer<ExchangeRateContainer.Output>
    ): ExchangeRateContainerInteractor =
        ExchangeRateContainerInteractor(
            buildParams = buildParams,
            globalStateFeature = globalStateFeature,
            input = input,
            output = output,
        )

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>,
        component: ExchangeRateContainerComponent,
    ): ExchangeRateContainerRouter =
        ExchangeRateContainerRouter(
            buildParams = buildParams,
            viewPagerBuilder = ViewPagerBuilder(component),
            appBarBuilder = AppBarBuilder(component)
        )

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: ExchangeRateContainer.Customisation,
        interactor: ExchangeRateContainerInteractor,
        router: ExchangeRateContainerRouter
    ): Node<ExchangeRateContainerView> = Node(
        buildParams = buildParams,
        viewFactory = customisation.viewFactory(null),
        plugins = listOf(interactor, router)
    )

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun viewPagerOutputConsumer(
        interactor: ExchangeRateContainerInteractor
    ): Consumer<ExchangeRate.Output> =
        interactor.exchangeRateOutputConsumer

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun viewPagerInputSource(
        interactor: ExchangeRateContainerInteractor
    ): ObservableSource<ExchangeRate.Input> =
        interactor.exchangeRateInputSource

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun appBarOutputConsumer(
        interactor: ExchangeRateContainerInteractor
    ): Consumer<AppBar.Output> =
        interactor.appBarOutputConsumer

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun appBarInputSource(
        interactor: ExchangeRateContainerInteractor
    ): ObservableSource<AppBar.Input> =
        interactor.appBarInputSource

    @ExchangeRateContainerScope
    @Provides
    @JvmStatic
    internal fun provideGlobalStateFeature() = GlobalStateFeature()
}
