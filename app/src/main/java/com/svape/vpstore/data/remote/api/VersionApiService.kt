package com.svape.vpstore.data.remote.api

import retrofit2.Response
import retrofit2.http.GET

interface VersionApiService {
    @GET("apicontrollerpruebas/api/ParametrosFramework/ConsultarParametrosFramework/VPStoreAppControl")
    suspend fun getVersion(): Response<String>
}