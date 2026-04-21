package com.svape.vpstore.data.repository

import com.svape.vpstore.data.local.dao.UserDao
import com.svape.vpstore.data.local.entity.UserEntity
import com.svape.vpstore.data.remote.api.AuthApiService
import com.svape.vpstore.data.remote.dto.LoginRequestDto
import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.User
import com.svape.vpstore.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApiService,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(): Resource<User> {
        return try {
            val headers = mapOf(
                "Usuario" to "pam.meredy21",
                "Identificacion" to "987204545",
                "Accept" to "text/json",
                "IdUsuario" to "pam.meredy21",
                "IdCentroServicio" to "1295",
                "NombreCentroServicio" to "PTO/BOGOTA/CUND/COL/OF PRINCIPAL - CRA 30 # 7-45",
                "IdAplicativoOrigen" to "9",
                "Content-Type" to "application/json"
            )
            val body = LoginRequestDto(
                password = "SW50ZXIyMDIx\n",
                username = "cGFtLm1lcmVkeTIx\n"
            )
            val response = api.login(headers, body)
            if (response.isSuccessful) {
                val dto = response.body()
                    ?: return Resource.Error("Empty response from server", 200)
                val username = dto.username
                    ?: return Resource.Error("Missing field: Usuario")

                val identification = dto.identification ?: "N/A"
                val fullName = listOfNotNull(dto.fullName, dto.lastName1, dto.lastName2)
                    .joinToString(" ")
                    .ifBlank { dto.username ?: "Unknown" }

                userDao.insert(
                    UserEntity(
                        username = username,
                        identification = identification,
                        fullName = fullName,
                        token = dto.token
                    )
                )
                Resource.Success(User(username, identification, fullName, dto.token))
            } else {
                Resource.Error("Auth error (${response.code()}): ${response.message()}", response.code())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun getLocalUser(): User? =
        userDao.getUser()?.let {
            User(it.username, it.identification, it.fullName, it.token)
        }

    override suspend fun logout() = userDao.clear()
}