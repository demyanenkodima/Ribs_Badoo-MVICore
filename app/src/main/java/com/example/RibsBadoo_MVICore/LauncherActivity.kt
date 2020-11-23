package com.example.RibsBadoo_MVICore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.RibsBadoo_MVICore.common.room.AppDatabase
import com.example.RibsBadoo_MVICore.common.room.createDefaultWallets
import com.example.RibsBadoo_MVICore.ribs.RootActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LauncherActivity : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(context = applicationContext)!!
        checkDatabase()
    }

    private fun checkDatabase() {
        compositeDisposable.add(
            database.getWalletDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { if (it.isEmpty()) createWallets() else login() }
                .subscribe()
        )
    }

    private fun login() {
        startActivity(Intent(this, RootActivity::class.java))
    }

    private fun createWallets() {
        compositeDisposable.add(
            database.getWalletDao()?.insertAll(createDefaultWallets())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete {
                    Log.e(localClassName, "Wallets is created")
                    login()
                }
                .subscribe()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}