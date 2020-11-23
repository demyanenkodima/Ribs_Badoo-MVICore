package com.example.RibsBadoo_MVICore.ribs.app_bar

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.badoo.ribs.core.customisation.inflate
import com.badoo.ribs.core.view.AndroidRibView
import com.badoo.ribs.core.view.RibView
import com.badoo.ribs.core.view.ViewFactory
import com.example.RibsBadoo_MVICore.R
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView.Event
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface AppBarView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object Exchange : Event()
    }

    data class ViewModel(
        val title: String
    )

    interface Factory : ViewFactory<Nothing?, AppBarView>
}

class AppBarViewImpl private constructor(
    override val androidView: ViewGroup,
    private val events: PublishRelay<Event> = PublishRelay.create()
) : AndroidRibView(),
    AppBarView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    private val exchange: Button = androidView.findViewById(R.id.btnExchange)
    private val toolbarTitle: TextView = androidView.findViewById(R.id.toolbar_title)

    init {
        exchange.setOnClickListener { events.accept(Event.Exchange) }
    }

    class Factory(
        @LayoutRes private val layoutRes: Int = R.layout.rib_app_bar
    ) : AppBarView.Factory {
        override fun invoke(deps: Nothing?): (RibView) -> AppBarView = {
            AppBarViewImpl(
                androidView = it.inflate(layoutRes)
            )
        }
    }

    override fun accept(vm: ViewModel) {
        toolbarTitle.text = vm.title
    }
}
