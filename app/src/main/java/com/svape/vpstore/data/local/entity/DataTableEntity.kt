package com.svape.vpstore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_schema_tables")
data class DataTableEntity(
    @PrimaryKey
    @ColumnInfo(name = "table_name") val tableName: String,
    @ColumnInfo(name = "description") val description: String? = null,
    @ColumnInfo(name = "record_count") val recordCount: Int? = null,
    @ColumnInfo(name = "last_updated") val lastUpdated: String? = null,
    @ColumnInfo(name = "is_active") val isActive: Boolean? = true,
    @ColumnInfo(name = "sync_timestamp") val syncTimestamp: Long = System.currentTimeMillis()
)