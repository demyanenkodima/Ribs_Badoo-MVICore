package com.example.RibsBadoo_MVICore.common.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.RibsBadoo_MVICore.common.room.models.converter.BigDecimalConverter
import com.example.RibsBadoo_MVICore.common.room.models.Wallet

@Database(entities = [Wallet::class], version = 1)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getWalletDao(): WalletDao

    companion object{
        private var INSTANCE: AppDatabase? = null

        open fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase::class.java, "wallet"
                        )
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        open fun destroyInstance() {
            synchronized(AppDatabase::class.java) { INSTANCE = null }
        }
    }


}
