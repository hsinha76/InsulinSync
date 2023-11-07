package com.hsdroid.insulinsync.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hsdroid.insulinsync.models.Insulin
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(insulin: Insulin)

    @Query("SELECT * from insulinLog order by id DESC")
    fun getAllData() : Flow<List<Insulin>>

    @Delete
    fun deleteData(insulin: Insulin)
}