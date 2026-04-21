package com.svape.vpstore.domain.usecase

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.Locality
import com.svape.vpstore.domain.repository.LocalitiesRepository
import javax.inject.Inject

class GetLocalitiesUseCase @Inject constructor(
    private val repository: LocalitiesRepository
) {
    suspend operator fun invoke(): Resource<List<Locality>> =
        repository.getLocalities()
}