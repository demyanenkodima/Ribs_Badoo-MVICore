package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerView
import dagger.BindsInstance

@ExchangeRateContainerScope
@dagger.Component(
    modules = [ExchangeRateContainerModule::class],
    dependencies = [ExchangeRateContainer.Dependency::class]
)
internal interface ExchangeRateContainerComponent : ExchangeRate.Dependency, AppBar.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: ExchangeRateContainer.Dependency,
            @BindsInstance customisation: ExchangeRateContainer.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): ExchangeRateContainerComponent
    }

    fun node(): Node<ExchangeRateContainerView>
}
