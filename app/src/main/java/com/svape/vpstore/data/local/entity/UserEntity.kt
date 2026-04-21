package com.svape.vpstore.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_user_session")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "identification") val identification: String,
    @ColumnInfo(name = "full_name") val fullName: String,
    @ColumnInfo(name = "token") val token: String? = null,
    @ColumnInfo(name = "login_timestamp") val loginTimestamp: Long = System.currentTimeMillis()
)