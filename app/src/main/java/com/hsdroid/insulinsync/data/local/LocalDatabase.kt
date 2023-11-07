package com.hsdroid.insulinsync.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hsdroid.insulinsync.data.local.dao.ProfileDao
import com.hsdroid.insulinsync.data.local.dao.StoreDao
import com.hsdroid.insulinsync.models.Insulin
import com.hsdroid.insulinsync.models.Profile

@Database(entities = [Insulin::class, Profile::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getDao(): StoreDao
    abstract fun getProfileDao() : ProfileDao
}