package com.example.RibsBadoo_MVICore.ribs.app_bar.mapper

import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView

object InputToViewModel : (AppBar.Input) -> AppBarView.ViewModel {
    override fun invoke(input: AppBar.Input): AppBarView.ViewModel =
        when (input) {
            is AppBar.Input.Title -> AppBarView.ViewModel(
                title = input.title
            )
        }
}
