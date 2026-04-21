package com.svape.vpstore.domain.model

data class User(
    val username: String,
    val identification: String,
    val fullName: String,
    val token: String? = null
)