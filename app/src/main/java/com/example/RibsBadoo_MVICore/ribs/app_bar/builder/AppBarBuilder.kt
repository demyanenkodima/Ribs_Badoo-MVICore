package com.example.RibsBadoo_MVICore.ribs.app_bar.builder

import com.badoo.ribs.builder.SimpleBuilder
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildParams
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBar
import com.example.RibsBadoo_MVICore.ribs.app_bar.AppBarView

class AppBarBuilder(
    private val dependency: AppBar.Dependency
) : SimpleBuilder<Node<AppBarView>>() {

    override fun build(buildParams: BuildParams<Nothing?>): Node<AppBarView> =
        DaggerAppBarComponent
            .factory()
            .create(
                dependency = dependency,
                customisation = AppBar.Customisation(),
                buildParams = buildParams
            )
            .node()
}
