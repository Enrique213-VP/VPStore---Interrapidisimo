package com.svape.vpstore.data.remote.api

import com.svape.vpstore.data.remote.dto.LoginRequestDto
import com.svape.vpstore.data.remote.dto.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface AuthApiService {
    @POST("FtEntregaElectronica/MultiCanales/ApiSeguridadPruebas/api/Seguridad/AuthenticaUsuarioApp")
    suspend fun login(
        @HeaderMap headers: Map<String, String>,
        @Body body: LoginRequestDto
    ): Response<LoginResponseDto>
}