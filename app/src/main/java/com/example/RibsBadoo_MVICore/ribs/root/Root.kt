package com.example.RibsBadoo_MVICore.ribs.root

import com.badoo.ribs.core.Rib
import com.example.RibsBadoo_MVICore.feature.CalculatorFeature
import com.example.RibsBadoo_MVICore.feature.ExchangeRateProviderFeature
import com.example.RibsBadoo_MVICore.feature.WalletFeature

interface Root : Rib {
    interface Dependency {
        val exchangeRateProviderFeature: ExchangeRateProviderFeature
        val calculatorFeature: CalculatorFeature
        val walletFeature: WalletFeature
    }

    sealed class Input
    sealed class Output
}
