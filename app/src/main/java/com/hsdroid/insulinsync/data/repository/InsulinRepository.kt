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

    fun insertData(insulin: Insulin) {
        storeDao.insertData(insulin)
    }

    fun getAllData(): Flow<List<Insulin>> {
        return storeDao.getAllData()
    }

    fun getProfileData(uname: String) : Flow<List<Profile>>{
        return profileDao.getProfile(uname)
    }
    fun deleteData(insulin: Insulin) {
        storeDao.deleteData(insulin)
    }
}