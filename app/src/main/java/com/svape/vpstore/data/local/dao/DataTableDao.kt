package com.svape.vpstore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.svape.vpstore.data.local.entity.DataTableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DataTableDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tables: List<DataTableEntity>)

    @Query("SELECT * FROM tbl_schema_tables ORDER BY table_name ASC")
    fun observeAll(): Flow<List<DataTableEntity>>

    @Query("SELECT COUNT(*) FROM tbl_schema_tables")
    suspend fun count(): Int

    @Query("DELETE FROM tbl_schema_tables")
    suspend fun clear()
}