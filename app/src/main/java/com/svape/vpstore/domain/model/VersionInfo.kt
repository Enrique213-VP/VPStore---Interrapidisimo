package com.svape.vpstore.domain.model

data class VersionInfo(
    val serverVersion: String,
    val localVersion: String,
    val status: VersionStatus
)

enum class VersionStatus {
    UP_TO_DATE,
    OUTDATED,
    AHEAD
}