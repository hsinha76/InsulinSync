package com.hsdroid.insulinsync.data.repository

import com.hsdroid.insulinsync.data.local.dao.ProfileDao
import com.hsdroid.insulinsync.data.local.dao.StoreDao
import com.hsdroid.insulinsync.models.Insulin
import com.hsdroid.insulinsync.models.Profile
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsulinRepository @Inject constructor(
    private val profileDao: ProfileDao,
    private val storeDao: StoreDao
) {

    fun insertProfileData(profile: Profile) {
        profileDao.addProfile(profile)
    }

    fun checkUserExists(uname: String): Boolean {
        return profileDao.checkUsernameExists(uname)
    }

    fun insertData(insulin: Insulin) {
        storeDao.insertData(insulin)
    }

    fun getAllData(uname: String): Flow<List<Insulin>> {
        return storeDao.getProfileData(uname)
    }

    fun getProfileData(): Flow<List<Profile>> {
        return profileDao.getProfile()
    }

    fun deleteData(insulin: Insulin) {
        storeDao.deleteData(insulin)
    }
}