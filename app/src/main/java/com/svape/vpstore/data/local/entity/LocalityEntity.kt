package com.svape.vpstore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_localities")
data class LocalityEntity(
    @PrimaryKey
    @ColumnInfo(name = "city_code") val cityCode: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "department") val department: String?
)