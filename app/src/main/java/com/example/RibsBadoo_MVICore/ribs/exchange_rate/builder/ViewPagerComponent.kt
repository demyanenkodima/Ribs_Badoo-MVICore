package com.example.RibsBadoo_MVICore.ribs.exchange_rate.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView
import dagger.BindsInstance

@ViewPagerScope
@dagger.Component(
    modules = [ViewPagerModule::class],
    dependencies = [ExchangeRate.Dependency::class]
)
internal interface ViewPagerComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: ExchangeRate.Dependency,
            @BindsInstance customisation: ExchangeRate.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): ViewPagerComponent
    }

    fun node(): Node<ExchangeRateView>
}
