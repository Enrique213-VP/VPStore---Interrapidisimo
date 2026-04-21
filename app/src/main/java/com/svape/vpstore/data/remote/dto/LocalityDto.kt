package com.svape.vpstore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocalityDto(
    @SerializedName("AbreviacionCiudad") val cityCode: String?,
    @SerializedName("NombreCompleto") val fullName: String?,
    @SerializedName("NombreCorto") val shortName: String?,
    @SerializedName("NombreAncestroPGrado") val department: String?,
    @SerializedName("CodigoPostal") val postalCode: String?,
    @SerializedName("PermiteRecogida") val allowsPickup: Boolean?,
    @SerializedName("IdLocalidad") val localityId: String?
)