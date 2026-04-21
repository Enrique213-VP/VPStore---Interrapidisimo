package com.svape.vpstore.domain.repository

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.DataTable
import kotlinx.coroutines.flow.Flow

interface DataTablesRepository {
    suspend fun syncSchema(token: String): Resource<List<DataTable>>
    fun getLocalTables(): Flow<List<DataTable>>
}