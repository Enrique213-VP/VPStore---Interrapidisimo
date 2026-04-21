package com.svape.vpstore.data.remote.api

import com.svape.vpstore.data.remote.dto.LocalityDto
import retrofit2.Response
import retrofit2.http.GET

interface LocalitiesApiService {
    @GET("apicontrollerpruebas/api/ParametrosFramework/ObtenerLocalidadesRecogidas")
    suspend fun getLocalities(): Response<List<LocalityDto>>
}