package com.svape.vpstore.domain.usecase

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.VersionInfo
import com.svape.vpstore.domain.repository.VersionRepository
import javax.inject.Inject

class CheckVersionUseCase @Inject constructor(
    private val repository: VersionRepository
) {
    suspend operator fun invoke(localVersion: String): Resource<VersionInfo> =
        repository.checkVersion(localVersion)
}