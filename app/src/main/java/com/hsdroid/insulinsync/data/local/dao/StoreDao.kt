package com.hsdroid.insulinsync.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hsdroid.insulinsync.models.Insulin
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertData(insulin: Insulin)

    @Query("SELECT * from insulinLog where uname = :uname order by id DESC")
    fun getProfileData(uname: String): Flow<List<Insulin>>

    @Delete
    fun deleteData(insulin: Insulin)
}