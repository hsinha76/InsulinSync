package com.hsdroid.insulinsync.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(val name: String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
