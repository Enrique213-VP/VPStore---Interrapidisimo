package com.svape.vpstore.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.svape.vpstore.domain.repository.AuthRepository
import com.svape.vpstore.presentation.home.HomeScreen
import com.svape.vpstore.presentation.localities.LocalitiesScreen
import com.svape.vpstore.presentation.login.LoginScreen
import com.svape.vpstore.presentation.tables.TablesScreen
import com.svape.vpstore.presentation.version.VersionScreen

object Routes {
    const val VERSION    = "version"
    const val LOGIN      = "login"
    const val HOME       = "home"
    const val TABLES     = "tables"
    const val LOCALITIES = "localities"
}

@Composable
fun VPStoreNavGraph(
    authRepository: AuthRepository,
    navController: NavHostController = rememberNavController()
) {
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val user = authRepository.getLocalUser()
        startDestination = if (user != null) Routes.HOME else Routes.VERSION
    }

    if (startDestination == null) return

    NavHost(navController = navController, startDestination = startDestination!!) {

        composable(Routes.VERSION) {
            VersionScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.VERSION) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToTables     = { navController.navigate(Routes.TABLES) },
                onNavigateToLocalities = { navController.navigate(Routes.LOCALITIES) },
                onLogout = {
                    navController.navigate(Routes.VERSION) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.TABLES) {
            TablesScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.LOCALITIES) {
            LocalitiesScreen(onBack = { navController.popBackStack() })
        }
    }
}