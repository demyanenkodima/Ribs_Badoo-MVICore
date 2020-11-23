package com.example.RibsBadoo_MVICore.ribs.app_bar

import com.badoo.ribs.clienthelper.connector.Connectable
import com.badoo.ribs.core.Rib
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar.Input
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar.Output
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

interface AppBar : Rib, Connectable<Input, Output> {

    interface Dependency {
        fun appBarInput(): ObservableSource<Input>
        fun appBarOutput(): Consumer<Output>
    }

    sealed class Input {
        data class Title(val title: String): Input()
    }

    sealed class Output {
        object Exchange : Output()
    }

    class Customisation(
        val viewFactory: AppBarView.Factory = AppBarViewImpl.Factory()
    )
}
