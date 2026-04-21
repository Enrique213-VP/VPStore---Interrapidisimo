package com.svape.vpstore

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.svape.vpstore.presentation.login.LoginScreen
import com.svape.vpstore.presentation.version.VersionScreen
import com.svape.vpstore.ui.theme.VPStoreTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.svape.vpstore", appContext.packageName)
    }

    @Test
    fun versionScreen_muestraNombreApp() {
        composeTestRule.setContent {
            VPStoreTheme {
                VersionScreen(onNavigateToLogin = {})
            }
        }
        composeTestRule.onNodeWithText("RAPIDÍSIMO").assertIsDisplayed()
    }

    @Test
    fun versionScreen_muestraIndicadorDeCarga() {
        composeTestRule.setContent {
            VPStoreTheme {
                VersionScreen(onNavigateToLogin = {})
            }
        }
        composeTestRule.onNodeWithText("Verificando versión...").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraBotonIniciarSesion() {
        composeTestRule.setContent {
            VPStoreTheme {
                LoginScreen(onLoginSuccess = {})
            }
        }
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun loginScreen_muestraUsuarioDePrueba() {
        composeTestRule.setContent {
            VPStoreTheme {
                LoginScreen(onLoginSuccess = {})
            }
        }
        composeTestRule.onNodeWithText("pam.meredy21").assertIsDisplayed()
    }

    @Test
    fun loginScreen_botonIniciarSesionEsClickeable() {
        composeTestRule.setContent {
            VPStoreTheme {
                LoginScreen(onLoginSuccess = {})
            }
        }
        composeTestRule.onNodeWithText("Iniciar Sesión").assertHasClickAction()
    }
}