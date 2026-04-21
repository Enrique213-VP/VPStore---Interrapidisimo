package com.svape.vpstore.presentation.version

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.svape.vpstore.domain.model.VersionStatus
import com.svape.vpstore.ui.theme.InterColors

@Composable
fun VersionScreen(
    onNavigateToLogin: () -> Unit,
    viewModel: VersionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.canNavigate) { if (state.canNavigate) onNavigateToLogin() }

    Box(
        modifier = Modifier.fillMaxSize().background(InterColors.DarkBg),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(500.dp).offset(y = (-100).dp).background(
                Brush.radialGradient(listOf(InterColors.Orange.copy(alpha = 0.08f), Color.Transparent)),
                CircleShape
            )
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth().padding(32.dp)) {
            Box(
                modifier = Modifier.size(88.dp).clip(RoundedCornerShape(22.dp))
                    .background(InterColors.Orange),
                contentAlignment = Alignment.Center
            ) { Icon(Icons.Default.LocalShipping, null, tint = Color.White, modifier = Modifier.size(48.dp)) }

            Spacer(Modifier.height(16.dp))
            Text("INTER", fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = InterColors.Orange, letterSpacing = 5.sp)
            Text("RAPIDÍSIMO", fontSize = 24.sp, fontWeight = FontWeight.Black, color = InterColors.White, letterSpacing = 1.sp)
            Text("Controller APP", style = MaterialTheme.typography.bodySmall, color = InterColors.GrayMedium)
            Spacer(Modifier.height(36.dp))

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = InterColors.DarkSurface),
                modifier = Modifier.fillMaxWidth().border(1.dp, InterColors.DarkBorder, RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(color = InterColors.Orange)
                            Spacer(Modifier.height(10.dp))
                            Text("Verificando versión...", color = InterColors.GrayMedium)
                        }
                        state.error != null -> {
                            VersionBadge(Icons.Default.WifiOff, InterColors.RedError, "Sin conexión", state.error!!)
                            Spacer(Modifier.height(16.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(onClick = { viewModel.checkVersion() }, modifier = Modifier.weight(1f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, InterColors.DarkBorder)
                                ) { Text("Reintentar", color = InterColors.White) }
                                Button(onClick = { viewModel.forceContinue() }, modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = InterColors.Orange)
                                ) { Text("Continuar") }
                            }
                        }
                        state.versionInfo != null -> {
                            val info = state.versionInfo!!
                            when (info.status) {
                                VersionStatus.UP_TO_DATE  -> VersionBadge(Icons.Default.CheckCircle, InterColors.GreenSuccess, "App actualizada", "Versión ${info.localVersion} al día")
                                VersionStatus.OUTDATED    -> VersionBadge(Icons.Default.SystemUpdate, InterColors.AmberWarn, "Actualización disponible", "Local: ${info.localVersion}  •  Servidor: ${info.serverVersion}\n\nPor favor actualiza la app.")
                                VersionStatus.AHEAD       -> VersionBadge(Icons.Default.Info, InterColors.BlueInfo, "Versión avanzada", "Tu versión (${info.localVersion}) supera la del servidor (${info.serverVersion}).")
                            }
                            Spacer(Modifier.height(20.dp))
                            Button(
                                onClick = { viewModel.forceContinue() },
                                colors = ButtonDefaults.buttonColors(containerColor = InterColors.Orange),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth().height(52.dp)
                            ) {
                                Text(if (info.status == VersionStatus.OUTDATED) "Continuar de todas formas" else "Continuar", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.width(6.dp))
                                Icon(Icons.Default.ArrowForward, null)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
            Text("© 2025 Interrapidísimo  •  Te la ponemos refácil", style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun VersionBadge(icon: ImageVector, tint: Color, title: String, message: String) {
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(tint.copy(alpha = 0.1f)).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(40.dp))
        Spacer(Modifier.height(8.dp))
        Text(title, fontWeight = FontWeight.Bold, color = InterColors.White)
        Spacer(Modifier.height(4.dp))
        Text(message, style = MaterialTheme.typography.bodySmall, color = InterColors.GrayMedium, textAlign = TextAlign.Center)
    }
}