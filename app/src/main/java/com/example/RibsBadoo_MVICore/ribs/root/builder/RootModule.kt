package com.example.RibsBadoo_MVICore.ribs.root.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.feature.CalculatorFeature
import com.example.RibsBadoo_MVICore.feature.ExchangeRateProviderFeature
import com.example.RibsBadoo_MVICore.feature.WalletFeature
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.builder.ExchangeRateContainerBuilder
import com.example.RibsBadoo_MVICore.ribs.root.RootInteractor
import com.example.RibsBadoo_MVICore.ribs.root.RootRouter
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object RootModule {

    @RootScope
    @Provides
    @JvmStatic
    internal fun interactor(
        exchangeRateProviderFeature: ExchangeRateProviderFeature,
        walletFeature: WalletFeature,
        calculatorFeature: CalculatorFeature,
        buildParams: BuildParams<Nothing?>
    ): RootInteractor =
        RootInteractor(
            buildParams = buildParams,
            exchangeRateProviderFeature = exchangeRateProviderFeature,
            calculatorFeature = calculatorFeature,
            walletFeature = walletFeature
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun router(
        buildParams: BuildParams<Nothing?>,
        component: RootComponent,
    ): RootRouter =
        RootRouter(
            exchangeRateContainerBuilder = ExchangeRateContainerBuilder(component),
            buildParams = buildParams
        )

    @RootScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        interactor: RootInteractor,
        router: RootRouter
    ): Node<Nothing> = Node(
        buildParams = buildParams,
        viewFactory = null,
        plugins = listOf(interactor, router)
    )

    @RootScope
    @Provides
    @JvmStatic
    internal fun viewPagerInputSource(
        interactor: RootInteractor
    ): ObservableSource<ExchangeRateContainer.Input> =
        interactor.exchangeRateContainerInputSource

    @RootScope
    @Provides
    @JvmStatic
    internal fun viewPagerOutputConsumer(
        interactor: RootInteractor
    ): Consumer<ExchangeRateContainer.Output> =
        interactor.exchangeRateContainerOutputConsumer
}
