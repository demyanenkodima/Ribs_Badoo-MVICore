package com.example.RibsBadoo_MVICore.ribs.app_bar

import androidx.lifecycle.Lifecycle
import com.badoo.binder.Binder
import com.badoo.binder.using
import com.badoo.ribs.clienthelper.interactor.Interactor
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.common.createDestroyLifecycle
import com.example.RibsBadoo_MVICore.ribs.app_bar.mapper.InputToViewModel
import com.example.RibsBadoo_MVICore.ribs.app_bar.mapper.ViewEventToOutput
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

internal class AppBarInteractor(
    private val output: Consumer<AppBar.Output>,
    private val input: ObservableSource<AppBar.Input>,
    buildParams: BuildParams<Nothing?>,
) : Interactor<AppBar, AppBarView>(
    buildParams = buildParams
) {
    override fun onViewCreated(view: AppBarView, viewLifecycle: Lifecycle) {
        Binder(viewLifecycle.createDestroyLifecycle()).apply {
            bind(view to output using ViewEventToOutput)
            bind(input to view using InputToViewModel)
        }
    }
}
