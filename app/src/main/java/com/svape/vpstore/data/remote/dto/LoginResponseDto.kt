package com.svape.vpstore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto(
    @SerializedName("Usuario") val username: String?,
    @SerializedName("Identificacion") val identification: String?,
    @SerializedName("Nombre") val fullName: String?,
    @SerializedName("Apellido1") val lastName1: String?,
    @SerializedName("Apellido2") val lastName2: String?,
    @SerializedName("Cargo") val position: String?,
    @SerializedName("TokenJWT") val token: String?,
    @SerializedName("MensajeResultado") val resultCode: Int?,
    @SerializedName("IdLocalidad") val localityId: String?,
    @SerializedName("NombreLocalidad") val localityName: String?,
    @SerializedName("NomRol") val roleName: String?,
    @SerializedName("IdRol") val roleId: String?
)