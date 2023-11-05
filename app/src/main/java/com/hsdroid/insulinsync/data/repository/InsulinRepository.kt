package com.hsdroid.insulinsync.data.repository

import com.hsdroid.insulinsync.data.local.dao.StoreDao
import com.hsdroid.insulinsync.models.Insulin
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class InsulinRepository @Inject constructor(private val storeDao: StoreDao) {

    fun insertData(insulin: Insulin) {
        storeDao.insertData(insulin)
    }

    fun getAllData(): Flow<List<Insulin>> {
        return storeDao.getAllData()
    }

    fun deleteData(insulin: Insulin) {
        storeDao.deleteData(insulin)
    }
}