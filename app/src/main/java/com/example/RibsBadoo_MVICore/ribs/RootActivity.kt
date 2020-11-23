package com.example.RibsBadoo_MVICore.ribs

import android.os.Bundle
import android.view.ViewGroup
import com.badoo.ribs.android.RibActivity
import com.badoo.ribs.core.Node
import com.badoo.ribs.core.modality.BuildContext
import com.example.RibsBadoo_MVICore.R
import com.example.RibsBadoo_MVICore.common.retrofit.ExchangeRateApi
import com.example.RibsBadoo_MVICore.common.room.AppDatabase
import com.example.RibsBadoo_MVICore.feature.CalculatorFeature
import com.example.RibsBadoo_MVICore.feature.ExchangeRateProviderFeature
import com.example.RibsBadoo_MVICore.feature.WalletFeature
import com.example.RibsBadoo_MVICore.ribs.root.Root
import com.example.RibsBadoo_MVICore.ribs.root.builder.RootBuilder

class RootActivity : RibActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_root)
        super.onCreate(savedInstanceState)
    }

    override val rootViewGroup: ViewGroup
        get() = findViewById(R.id.root)

    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        RootBuilder(
            object : Root.Dependency {
                override val exchangeRateProviderFeature = ExchangeRateProviderFeature(
                    ExchangeRateApi.service
                )
                override val calculatorFeature = CalculatorFeature()
                override val walletFeature =
                    WalletFeature(AppDatabase.getDatabase(this@RootActivity)!!.getWalletDao())
            }
        ).build(
            buildContext = BuildContext.root(
                savedInstanceState = savedInstanceState
            )
        )
}