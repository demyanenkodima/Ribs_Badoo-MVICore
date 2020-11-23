package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container

import android.os.Parcelable
import com.badoo.ribs.core.modality.BuildParams
import com.badoo.ribs.routing.Routing
import com.badoo.ribs.routing.action.AttachRibRoutingAction.Companion.attach
import com.badoo.ribs.routing.action.RoutingAction
import com.badoo.ribs.routing.router.Router
import com.badoo.ribs.routing.source.RoutingSource.Companion.permanent
import com.badoo.ribs.routing.transition.handler.TransitionHandler
import com.example.RibsBadoo_MVICore.ribs.app_bar.builder.AppBarBuilder
import com.example.RibsBadoo_MVICore.ribs.exchange_rate.builder.ViewPagerBuilder
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerRouter.Configuration
import com.example.RibsBadoo_MVICore.ribs.exchange_rate_container.ExchangeRateContainerRouter.Configuration.Permanent
import kotlinx.android.parcel.Parcelize

class ExchangeRateContainerRouter internal constructor(
    buildParams: BuildParams<*>,
    private val viewPagerBuilder: ViewPagerBuilder,
    private val appBarBuilder: AppBarBuilder,
    transitionHandler: TransitionHandler<Configuration>? = null
) : Router<Configuration>(
    buildParams = buildParams,
    routingSource = permanent(Permanent.AppBar, Permanent.ExchangeRate),
    transitionHandler = transitionHandler
) {
    sealed class Configuration : Parcelable {
        sealed class Permanent : Configuration() {
            @Parcelize
            object AppBar : Configuration()

            @Parcelize
            object ExchangeRate : Configuration()
        }
    }

    override fun resolve(routing: Routing<Configuration>): RoutingAction =
        when (routing.configuration) {
            is Permanent.AppBar -> {
                attach { appBarBuilder.build(it) }
            }
            is Permanent.ExchangeRate -> {
                attach { viewPagerBuilder.build(it) }
            }
        }
}