package com.example.RibsBadoo_MVICore.common.room.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.RibsBadoo_MVICore.common.room.models.converter.BigDecimalConverter
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
@Entity(tableName = "wallet")
data class Wallet(
    val currency: String,
    @TypeConverters(BigDecimalConverter::class)
    var balance: BigDecimal
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}