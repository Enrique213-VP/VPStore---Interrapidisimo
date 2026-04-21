package com.svape.vpstore.data.repository

import com.svape.vpstore.data.local.dao.LocalityDao
import com.svape.vpstore.data.local.entity.LocalityEntity
import com.svape.vpstore.data.remote.api.LocalitiesApiService
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.Locality
import com.svape.vpstore.domain.repository.LocalitiesRepository
import javax.inject.Inject

class LocalitiesRepositoryImpl @Inject constructor(
    private val api: LocalitiesApiService,
    private val dao: LocalityDao
) : LocalitiesRepository {

    override suspend fun getLocalities(): Resource<List<Locality>> {
        return try {
            val response = api.getLocalities()

            if (response.isSuccessful) {
                val localities = response.body()
                    ?.mapNotNull { dto ->
                        val code = dto.cityCode ?: return@mapNotNull null
                        val name = dto.shortName?.trim()
                            ?: dto.fullName?.replace("\\", " / ")?.trim()
                            ?: return@mapNotNull null
                        Locality(code, name, dto.department)
                    } ?: emptyList()

                dao.clear()
                dao.insertAll(localities.map {
                    LocalityEntity(it.cityCode, it.fullName, it.department)
                })

                Resource.Success(localities)
            } else {
                loadFromCache("Error ${response.code()}: ${response.message()}")
            }
        } catch (e: Exception) {
            loadFromCache(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun loadFromCache(networkError: String): Resource<List<Locality>> {
        val cached = dao.getAll()
        return if (cached.isNotEmpty()) {
            Resource.Success(cached.map { Locality(it.cityCode, it.fullName, it.department) })
        } else {
            Resource.Error(networkError)
        }
    }
}