package com.svape.vpstore.di

import android.content.Context
import androidx.room.Room
import com.svape.vpstore.data.local.dao.DataTableDao
import com.svape.vpstore.data.local.dao.LocalityDao
import com.svape.vpstore.data.local.dao.UserDao
import com.svape.vpstore.data.local.database.VPStoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): VPStoreDatabase =
        Room.databaseBuilder(
            context,
            VPStoreDatabase::class.java,
            VPStoreDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideUserDao(db: VPStoreDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideDataTableDao(db: VPStoreDatabase): DataTableDao = db.dataTableDao()

    @Provides
    @Singleton
    fun provideLocalityDao(db: VPStoreDatabase): LocalityDao = db.localityDao()
}