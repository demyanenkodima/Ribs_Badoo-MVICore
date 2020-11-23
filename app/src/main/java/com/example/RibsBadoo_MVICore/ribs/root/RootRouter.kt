package com.example.RibsBadoo_MVICore.ribs.root

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.builder.ExchangeRateContainerBuilder
import kotlinx.android.parcel.Parcelize

class RootRouter(
    private val exchangeRateContainerBuilder: ExchangeRateContainerBuilder,
    buildParams: BuildParams<Nothing?>,
) : Router<RootRouter.Configuration>(
    buildParams = buildParams,
    routingSource = RoutingSource.permanent(
        Configuration.Permanent.ExchangeRateContainer
    )
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            object ExchangeRateContainer : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            is Configuration.Permanent.ExchangeRateContainer -> {
                attach { exchangeRateContainerBuilder.build(it) }
            }
        }
}
