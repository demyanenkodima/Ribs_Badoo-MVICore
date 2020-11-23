package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainer
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerView

class ExchangeRateContainerBuilder(
    private val dependency: ExchangeRateContainer.Dependency
) : SimpleBuilder<Node<ExchangeRateContainerView>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<ExchangeRateContainerView> =
        DaggerExchangeRateContainerComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = ExchangeRateContainer.Customisation(),
                buildParams = buildParams
            )
            .node()
}
