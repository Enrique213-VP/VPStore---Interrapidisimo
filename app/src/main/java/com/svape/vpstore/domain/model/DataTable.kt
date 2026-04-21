package com.svape.vpstore.domain.model

data class DataTable(
    val tableName: String,
    val description: String?,
    val recordCount: Int?,
    val lastUpdated: String?,
    val isActive: Boolean?
)