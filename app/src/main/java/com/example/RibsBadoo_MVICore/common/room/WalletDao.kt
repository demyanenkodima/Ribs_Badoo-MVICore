package com.example.RibsBadoo_MVICore.common.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.RibsBadoo_MVICore.common.room.models.Wallet
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallet")
    fun getAll(): Maybe<List<Wallet>>

    @Query("SELECT * FROM wallet WHERE currency = :currency LIMIT 1")
    fun get(currency: String): Maybe<Wallet>

    @Insert
    fun create(wallet: Wallet): Completable

    @Insert
    @JvmSuppressWildcards
    fun insertAll(walletList: List<Wallet>): Completable

    @Update
    fun update(wallet: Wallet): Completable
}