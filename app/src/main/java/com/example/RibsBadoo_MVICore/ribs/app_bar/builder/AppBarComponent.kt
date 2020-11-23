package com.example.RibsBadoo_MVICore.ribs.app_bar.builder

import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView

import dagger.BindsInstance

@AppBarScope
@dagger.Component(
    modules = [AppBarModule::class],
    dependencies = [AppBar.Dependency::class]
)
internal interface AppBarComponent {

    @dagger.Component.Factory
    interface Factory {
        fun create(
            dependency: AppBar.Dependency,
            @BindsInstance customisation: AppBar.Customisation,
            @BindsInstance buildParams: BuildParams<Nothing?>
        ): AppBarComponent
    }

    fun node(): Node<AppBarView>
}
