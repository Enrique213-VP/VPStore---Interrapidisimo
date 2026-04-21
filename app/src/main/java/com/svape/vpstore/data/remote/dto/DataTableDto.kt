package com.svape.vpstore.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DataTableDto(
    @SerializedName("NombreTabla") val tableName: String?,
    @SerializedName("Descripcion") val description: String?,
    @SerializedName("CantidadRegistros") val recordCount: Int?,
    @SerializedName("FechaUltimaActualizacion") val lastUpdated: String?,
    @SerializedName("Activo") val isActive: Boolean?
)