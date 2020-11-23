package com.example.RibsBadoo_MVICore.ribs.exchange_rate_container

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.example.RibsBadoo_MVICore.R

interface ExchangeRateContainerView : RibView {

    interface Factory : ViewFactory<Nothing?, ExchangeRateContainerView>
}


class ExchangeRateContainerViewImpl private constructor(
    override val androidView: ViewGroup
) : AndroidRibView(),
    ExchangeRateContainerView {

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_exchange_rate_container
    ) : ExchangeRateContainerView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> ExchangeRateContainerView = {
            ExchangeRateContainerViewImpl(
                it.inflate(layoutRes)
            )
        }
    }



//    private val photoFeedContainer = androidView.findViewById<ViewGroup>(R.id.photoFeedContainer)

//    override fun getParentViewForChild(child: Node<*>): ViewGroup =
//        when (child) {
//            is ExchangeRateContainer -> photoFeedContainer
//            else -> super.getParentViewForChild(child)
//        }
}
