package com.svape.vpstore.domain.usecase

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.DataTable
import com.svape.vpstore.domain.repository.DataTablesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTablesUseCase @Inject constructor(
    private val repository: DataTablesRepository
) {
    suspend fun sync(token: String): Resource<List<DataTable>> = repository.syncSchema(token)
    fun getLocal(): Flow<List<DataTable>> = repository.getLocalTables()
}