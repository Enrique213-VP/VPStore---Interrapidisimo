package com.svape.vpstore.data.repository

import com.svape.vpstore.data.local.dao.DataTableDao
import com.svape.vpstore.data.local.entity.DataTableEntity
import com.svape.vpstore.data.remote.api.DataTablesApiService
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.DataTable
import com.svape.vpstore.domain.repository.DataTablesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataTablesRepositoryImpl @Inject constructor(
    private val api: DataTablesApiService,
    private val dao: DataTableDao
) : DataTablesRepository {

    override suspend fun syncSchema(token: String): Resource<List<DataTable>> {
        return try {
            val bearerToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.getSchema(bearerToken)
            if (response.isSuccessful) {
                val dtos = response.body() ?: emptyList()
                dao.clear()
                val entities = dtos.mapNotNull { dto ->
                    dto.tableName?.let { name ->
                        DataTableEntity(
                            tableName = name,
                            description = dto.description,
                            recordCount = dto.recordCount,
                            lastUpdated = dto.lastUpdated,
                            isActive = dto.isActive
                        )
                    }
                }
                dao.insertAll(entities)
                Resource.Success(entities.map { it.toDomain() })
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override fun getLocalTables(): Flow<List<DataTable>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    private fun DataTableEntity.toDomain() = DataTable(
        tableName = tableName,
        description = description,
        recordCount = recordCount,
        lastUpdated = lastUpdated,
        isActive = isActive
    )
}