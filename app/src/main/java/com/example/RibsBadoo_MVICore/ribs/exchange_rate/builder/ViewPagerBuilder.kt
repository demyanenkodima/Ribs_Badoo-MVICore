package com.example.RibsBadoo_MVICore.ribs.exchange_rate.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.ExchangeRate
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.view.ExchangeRateView

class ViewPagerBuilder(
    private val dependency: ExchangeRate.Dependency
) : SimpleBuilder<Node<ExchangeRateView>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<ExchangeRateView> =
        DaggerViewPagerComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = ExchangeRate.Customisation(),
                buildParams = buildParams
            )
            .node()
}
