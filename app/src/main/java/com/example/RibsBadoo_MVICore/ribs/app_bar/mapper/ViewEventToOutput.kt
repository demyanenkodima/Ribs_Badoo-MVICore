package com.example.RibsBadoo_MVICore.ribs.app_bar.mapper

import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar.Output
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView.Event

internal object ViewEventToOutput : (Event) -> Output? {
    override fun invoke(event: Event): Output? =
        when (event) {
            is Event.Exchange -> Output.Exchange
        }
}
