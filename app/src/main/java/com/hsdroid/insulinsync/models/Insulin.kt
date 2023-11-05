package com.hsdroid.insulinsync.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insulinLog")
data class Insulin(
    val userName: String?,
    val insulinName: String?,
    val quantityTotal: Long?,
    val quantityLeft: Long?,
    val dosageUnit: Long?,
    val dosageTime: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
