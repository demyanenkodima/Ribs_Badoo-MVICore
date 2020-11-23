package com.example.RibsBadoo_MVICore.ribs.app_bar.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarInteractor
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView
import dagger.Provides
import io.reactivex.ObservableSource
import io.reactivex.functions.Consumer

@dagger.Module
internal object AppBarModule {

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun interactor(
        buildParams: BuildParams<Nothing?>,
        output: Consumer<AppBar.Output>,
        input: ObservableSource<AppBar.Input>,
    ): AppBarInteractor =
        AppBarInteractor(
            buildParams = buildParams,
            output = output,
            input = input
        )

    @AppBarScope
    @Provides
    @JvmStatic
    internal fun node(
        buildParams: BuildParams<Nothing?>,
        customisation: AppBar.Customisation,
        interactor: AppBarInteractor
    ): Node<AppBarView> =
        Node(
            buildParams = buildParams,
            viewFactory = customisation.viewFactory(null),
            plugins = listOf(interactor)
        )
}
