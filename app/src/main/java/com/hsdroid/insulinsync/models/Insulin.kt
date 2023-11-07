package com.hsdroid.insulinsync.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insulinLog")
data class Insulin(
    val uname: String,
    val insulinName: String,
    val quantityTotal: Float,
    var quantityLeft: Float,
    val dosageUnit: Float,
    val dosageTime: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
