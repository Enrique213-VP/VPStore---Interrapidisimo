package com.svape.vpstore.domain.repository

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.Locality

interface LocalitiesRepository {
    suspend fun getLocalities(): Resource<List<Locality>>
}