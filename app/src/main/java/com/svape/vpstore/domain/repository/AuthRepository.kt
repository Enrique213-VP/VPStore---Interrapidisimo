package com.svape.vpstore.domain.repository

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.User

interface AuthRepository {
    suspend fun login(): Resource<User>
    suspend fun getLocalUser(): User?
    suspend fun logout()
}