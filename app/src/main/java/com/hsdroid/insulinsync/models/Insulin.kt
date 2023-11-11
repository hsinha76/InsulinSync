package com.hsdroid.insulinsync.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "insulinLog")
@Parcelize
data class Insulin(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val uname: String,
    val insulinName: String,
    val quantityTotal: Float,
    var quantityLeft: Float,
    val dosageUnit: Float,
    val dosageTime: Long
) : Parcelable