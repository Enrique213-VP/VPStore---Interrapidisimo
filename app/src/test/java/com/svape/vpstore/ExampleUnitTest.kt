package com.svape.vpstore

import com.svape.vpstore.domain.common.Resource
import com.svape.vpstore.domain.model.DataTable
import com.svape.vpstore.domain.model.User
import com.svape.vpstore.domain.model.VersionStatus
import com.svape.vpstore.domain.repository.AuthRepository
import com.svape.vpstore.domain.repository.DataTablesRepository
import com.svape.vpstore.domain.usecase.GetTablesUseCase
import com.svape.vpstore.presentation.tables.TablesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `version local menor que servidor retorna OUTDATED`() {
        assertEquals(VersionStatus.OUTDATED, compareVersions("1.0.0", "100"))
    }

    @Test
    fun `version local igual al servidor retorna UP_TO_DATE`() {
        assertEquals(VersionStatus.UP_TO_DATE, compareVersions("1.0.0", "1.0.0"))
    }

    @Test
    fun `version local mayor al servidor retorna AHEAD`() {
        assertEquals(VersionStatus.AHEAD, compareVersions("2.0.0", "1.0.0"))
    }

    @Test
    fun `version con patch diferente compara correctamente`() {
        assertEquals(VersionStatus.OUTDATED, compareVersions("1.0.1", "1.0.2"))
    }

    @Test
    fun `cuando el servidor devuelve 401 el viewmodel expone error en uiState`() = runTest {
        val fakeTablesRepo = object : DataTablesRepository {
            override suspend fun syncSchema(token: String): Resource<List<DataTable>> =
                Resource.Error("Error 401: No está autorizado. Consulte con el administrador.", 401)
            override fun getLocalTables() = flowOf(emptyList<DataTable>())
        }

        val fakeAuthRepo = fakeAuthRepoConToken(token = null)
        val viewModel = TablesViewModel(GetTablesUseCase(fakeTablesRepo), fakeAuthRepo)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNotNull("El error no debe ser null cuando hay 401", state.error)
        assertTrue("El mensaje debe contener 401", state.error!!.contains("401"))
        assertTrue("No debe haber tablas", state.tables.isEmpty())
        assertTrue("No debe estar sincronizando", !state.isSyncing)
    }

    @Test
    fun `cuando TokenJWT es null se envia header Bearer vacio`() = runTest {
        var tokenCapturado: String? = null

        val fakeTablesRepo = object : DataTablesRepository {
            override suspend fun syncSchema(token: String): Resource<List<DataTable>> {
                tokenCapturado = token
                return Resource.Error("Error 401: No está autorizado.", 401)
            }
            override fun getLocalTables() = flowOf(emptyList<DataTable>())
        }

        val fakeAuthRepo = fakeAuthRepoConToken(token = null)
        TablesViewModel(GetTablesUseCase(fakeTablesRepo), fakeAuthRepo)

        advanceUntilIdle()

        assertEquals(
            "Con TokenJWT=null el header debe ser 'Bearer ' (vacío)",
            "Bearer ",
            tokenCapturado
        )
    }

    @Test
    fun `cuando hay token valido se envia en el header correctamente`() = runTest {
        var tokenCapturado: String? = null
        val tokenReal = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test"

        val fakeTablesRepo = object : DataTablesRepository {
            override suspend fun syncSchema(token: String): Resource<List<DataTable>> {
                tokenCapturado = token
                return Resource.Success(emptyList())
            }
            override fun getLocalTables() = flowOf(emptyList<DataTable>())
        }

        val fakeAuthRepo = fakeAuthRepoConToken(token = tokenReal)
        TablesViewModel(GetTablesUseCase(fakeTablesRepo), fakeAuthRepo)

        advanceUntilIdle()

        assertEquals(
            "Con token válido debe enviarse 'Bearer {token}'",
            "Bearer $tokenReal",
            tokenCapturado
        )
    }

    @Test
    fun `cuando el sync es exitoso el mensaje de exito aparece en uiState`() = runTest {
        val tablasSimuladas = listOf(
            DataTable("TBL_USUARIOS", "Usuarios del sistema", 150, null, true),
            DataTable("TBL_GUIAS",    "Guías de envío",       5420, null, true)
        )

        val fakeTablesRepo = object : DataTablesRepository {
            override suspend fun syncSchema(token: String) = Resource.Success(tablasSimuladas)
            override fun getLocalTables() = flowOf(tablasSimuladas)
        }

        val fakeAuthRepo = fakeAuthRepoConToken(token = "token-valido")
        val viewModel = TablesViewModel(GetTablesUseCase(fakeTablesRepo), fakeAuthRepo)

        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("El mensaje de éxito debe contener el número de tablas", state.successMessage?.contains("2") == true)
        assertTrue("No debe haber error", state.error == null)
    }

    private fun fakeAuthRepoConToken(token: String?) = object : AuthRepository {
        override suspend fun login() = Resource.Error("not used")
        override suspend fun getLocalUser() = User(
            username       = "pam.meredy21",
            identification = "987204545",
            fullName       = "pam.meredy21",
            token          = token
        )
        override suspend fun logout() {}
    }

    private fun compareVersions(local: String, server: String): VersionStatus {
        val l = local.split(".").mapNotNull { it.toIntOrNull() }
        val s = server.split(".").mapNotNull { it.toIntOrNull() }
        for (i in 0 until maxOf(l.size, s.size)) {
            val lv = l.getOrElse(i) { 0 }
            val sv = s.getOrElse(i) { 0 }
            if (lv < sv) return VersionStatus.OUTDATED
            if (lv > sv) return VersionStatus.AHEAD
        }
        return VersionStatus.UP_TO_DATE
    }
}