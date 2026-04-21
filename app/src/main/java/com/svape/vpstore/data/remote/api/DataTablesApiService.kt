package com.svape.vpstore.data.remote.api

import com.svape.vpstore.data.remote.dto.DataTableDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DataTablesApiService {
    @GET("apicontrollerpruebas/api/SincronizadorDatos/ObtenerEsquema/true")
    suspend fun getSchema(
        @Header("Authorization") token: String
    ): Response<List<DataTableDto>>
}