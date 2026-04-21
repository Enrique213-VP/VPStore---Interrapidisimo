package com.svape.vpstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.svape.vpstore.domain.repository.AuthRepository
import com.svape.vpstore.navigation.VPStoreNavGraph
import com.svape.vpstore.ui.theme.VPStoreTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VPStoreTheme {
                VPStoreNavGraph(authRepository = authRepository)
            }
        }
    }
}