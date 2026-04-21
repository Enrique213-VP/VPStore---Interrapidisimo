package com.svape.vpstore.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import com.svape.vpstore.ui.theme.InterColors

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    LaunchedEffect(state.loginSuccess) { if (state.loginSuccess) onLoginSuccess() }

    state.error?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            containerColor = InterColors.DarkSurface,
            icon = { Icon(Icons.Default.ErrorOutline, null, tint = InterColors.RedError, modifier = Modifier.size(40.dp)) },
            title = { Text("Error de autenticación", fontWeight = FontWeight.Bold, color = InterColors.White) },
            text = { Text(error, textAlign = TextAlign.Center, color = InterColors.GrayLight) },
            confirmButton = {
                Button(onClick = { viewModel.clearError() }, colors = ButtonDefaults.buttonColors(containerColor = InterColors.Orange)) {
                    Text("Aceptar")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(InterColors.DarkBg)) {
        Box(
            modifier = Modifier.size(400.dp).offset(y = 100.dp).align(Alignment.BottomCenter).background(
                Brush.radialGradient(listOf(InterColors.Orange.copy(alpha = 0.06f), Color.Transparent))
            )
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
                    .background(Brush.verticalGradient(listOf(InterColors.Orange, Color(0xFFCC4500))))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.LocalShipping, null, tint = Color.White, modifier = Modifier.size(52.dp))
                    Spacer(Modifier.height(12.dp))
                    Text("INTER", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.8f), letterSpacing = 5.sp)
                    Text("RAPIDÍSIMO", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color.White, letterSpacing = 1.sp)
                    Spacer(Modifier.height(4.dp))
                    Text("Ingresa al sistema", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f))
                }
            }

            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth().border(1.dp, InterColors.DarkBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = InterColors.DarkSurface)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Credenciales de acceso", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InterColors.White)
                    Text("Ambiente de pruebas", style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
                    Spacer(Modifier.height(16.dp))

                    LoginRow(Icons.Default.AccountCircle, "Usuario",            "pam.meredy21")
                    Spacer(Modifier.height(8.dp))
                    LoginRow(Icons.Default.Business,      "Centro de servicio", "1295 – OF PRINCIPAL")
                    Spacer(Modifier.height(8.dp))
                    LoginRow(Icons.Default.PhoneAndroid,  "Aplicación",         "Controller APP")

                    Spacer(Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.login() },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth().height(54.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = InterColors.Orange)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                            Spacer(Modifier.width(10.dp))
                            Text("Autenticando...")
                        } else {
                            Icon(Icons.Default.Login, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Iniciar Sesión", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
            .background(InterColors.DarkCard).padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = InterColors.Orange, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = InterColors.White)
        }
    }
}