package com.svape.vpstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.svape.vpstore.data.local.entity.LocalityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(localities: List<LocalityEntity>)

    @Query("SELECT * FROM tbl_localities ORDER BY full_name ASC")
    suspend fun getAll(): List<LocalityEntity>

    @Query("SELECT COUNT(*) FROM tbl_localities")
    suspend fun count(): Int

    @Query("DELETE FROM tbl_localities")
    suspend fun clear()
}