package com.hsdroid.insulinsync.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hsdroid.insulinsync.data.local.dao.StoreDao
import com.hsdroid.insulinsync.models.Insulin

@Database(entities = [Insulin::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDao(): StoreDao
}