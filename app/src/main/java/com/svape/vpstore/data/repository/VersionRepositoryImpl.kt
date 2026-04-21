package com.svape.vpstore.data.repository

import com.svape.vpstore.data.remote.api.VersionApiService
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.VersionInfo
import com.svape.vpstore.domain.model.VersionStatus
import com.svape.vpstore.domain.repository.VersionRepository
import javax.inject.Inject

class VersionRepositoryImpl @Inject constructor(
    private val api: VersionApiService
) : VersionRepository {

    override suspend fun checkVersion(localVersion: String): Resource<VersionInfo> {
        return try {
            val response = api.getVersion()
            if (response.isSuccessful) {
                val raw = response.body()?.trim()?.removeSurrounding("\"")
                    ?: return Resource.Error("Could not read server version")
                Resource.Success(
                    VersionInfo(
                        serverVersion = raw,
                        localVersion = localVersion,
                        status = compare(localVersion, raw)
                    )
                )
            } else {
                Resource.Error("Error ${response.code()}: ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private fun compare(local: String, server: String): VersionStatus {
        val l = local.split(".").mapNotNull { it.toIntOrNull() }
        val s = server.split(".").mapNotNull { it.toIntOrNull() }
        for (i in 0 until maxOf(l.size, s.size)) {
            val lv = l.getOrElse(i) { 0 }
            val sv = s.getOrElse(i) { 0 }
            if (lv < sv) return VersionStatus.OUTDATED
            if (lv > sv) return VersionStatus.AHEAD
        }
        return VersionStatus.UP_TO_DATE
    }
}