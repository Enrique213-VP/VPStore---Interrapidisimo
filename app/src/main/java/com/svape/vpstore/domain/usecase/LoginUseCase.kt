package com.svape.vpstore.domain.usecase

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.User
import com.svape.vpstore.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(): Resource<User> =
        repository.login()
}