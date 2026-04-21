package com.svape.vpstore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class VersionParameterDto(
    @SerializedName("IdParametro") val id: String?,
    @SerializedName("Valor") val value: String?,
    @SerializedName("Descripcion") val description: String?
)