package com.svape.vpstore.domain.repository

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.VersionInfo

interface VersionRepository {
    suspend fun checkVersion(localVersion: String): Resource<VersionInfo>
}