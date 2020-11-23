package com.example.RibsBadoo_MVICore.common.room

open class WalletRepository(private val roomDatabase: AppDatabase) {
    open fun productsDao(): WalletDao = roomDatabase.getWalletDao()
}