package com.svape.vpstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.svape.vpstore.data.local.dao.DataTableDao
import com.svape.vpstore.data.local.dao.LocalityDao
import com.svape.vpstore.data.local.dao.UserDao
import com.svape.vpstore.data.local.entity.DataTableEntity
import com.svape.vpstore.data.local.entity.LocalityEntity
import com.svape.vpstore.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        DataTableEntity::class,
        LocalityEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class VPStoreDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dataTableDao(): DataTableDao
    abstract fun localityDao(): LocalityDao

    companion object {
        const val DATABASE_NAME = "vpstore_db"
    }
}