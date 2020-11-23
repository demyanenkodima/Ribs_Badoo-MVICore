package com.example.RibsBadoo_MVICore.ribs.root.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.root.Root
import dagger.BindsInstance

@RootScope
@dagger.Component(
    modules = [RootModule::class],
    dependencies = [Root.Dependency::class]
)
internal interface RootComponent : ExchangeRateContainer.Dependency {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: Root.Dependency,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): RootComponent
    }

    fun node(): Node<Nothing>
}
