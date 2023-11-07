package com.hsdroid.insulinsync.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.hsdroid.insulinsync.data.local.LocalDatabase
import com.hsdroid.insulinsync.data.local.dao.ProfileDao
import com.hsdroid.insulinsync.data.local.dao.StoreDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun getRoomDB(context: Application) =
        Room.databaseBuilder(context, LocalDatabase::class.java, "InsulinSync")
            .fallbackToDestructiveMigrationOnDowngrade().build()

    @Singleton
    @Provides
    fun getDao(db: LocalDatabase): StoreDao {
        return db.getDao()
    }

    @Singleton
    @Provides
    fun getProfileDao(db: LocalDatabase) : ProfileDao{
        return db.getProfileDao()
    }

    @Provides
    fun providesContext(context: Application): Context {
        return context.applicationContext
    }
}