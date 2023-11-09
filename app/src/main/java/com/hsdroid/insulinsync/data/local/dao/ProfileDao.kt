package com.hsdroid.insulinsync.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hsdroid.insulinsync.models.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Insert
    fun addProfile(profile: Profile)

    @Query("SELECT * from profile order by id ASC")
    fun getProfile(): Flow<List<Profile>>

    @Query("SELECT EXISTS(SELECT * from profile where name = :uname)")
    fun checkUsernameExists(uname: String): Boolean
}