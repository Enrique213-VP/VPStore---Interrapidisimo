package com.svape.vpstore.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.svape.vpstore.ui.theme.InterColors

@Composable
fun HomeScreen(
    onNavigateToTables: () -> Unit,
    onNavigateToLocalities: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.loggedOut) {
        if (state.loggedOut) onLogout()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(InterColors.DarkBg).verticalScroll(rememberScrollState())
    ) {
        // Header naranja
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Brush.verticalGradient(listOf(InterColors.Orange, Color(0xFFCC4500))))
                .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 48.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) { Icon(Icons.Default.LocalShipping, null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("INTER", fontSize = 10.sp, fontWeight = FontWeight.ExtraBold, color = Color.White.copy(alpha = 0.8f), letterSpacing = 4.sp)
                        Text("RAPIDÍSIMO", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color.White)
                    }
                    IconButton(onClick = { viewModel.logout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Cerrar sesión", tint = Color.White)
                    }
                }
                Spacer(Modifier.height(20.dp))
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp))
                } else {
                    state.user?.let {
                        Text("¡Hola, ${it.fullName.split(" ").first()}!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Bienvenido al sistema", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.75f))
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

            // Tarjeta de datos del usuario
            state.user?.let { user ->
                Card(
                    modifier = Modifier.fillMaxWidth().border(1.dp, InterColors.DarkBorder, RoundedCornerShape(18.dp)),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = InterColors.DarkSurface)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(8.dp).clip(CircleShape).background(InterColors.Orange)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Datos de sesión", style = MaterialTheme.typography.labelMedium, color = InterColors.GrayMedium)
                        }
                        Spacer(Modifier.height(14.dp))
                        UserRow(Icons.Default.AccountCircle, "Usuario",        user.username)
                        Spacer(Modifier.height(1.dp).background(InterColors.DarkBorder).fillMaxWidth())
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = InterColors.DarkBorder)
                        UserRow(Icons.Default.Badge,         "Identificación", user.identification)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = InterColors.DarkBorder)
                        UserRow(Icons.Default.Person,        "Nombre",         user.fullName)
                    }
                }
            }

            // Sección módulos
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(3.dp).height(18.dp).clip(RoundedCornerShape(2.dp)).background(InterColors.Orange))
                Spacer(Modifier.width(10.dp))
                Text("Módulos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InterColors.White)
            }

            // Botón Tablas
            ModuleCard(
                title    = "Tablas",
                subtitle = "Esquema de datos sincronizado",
                icon     = Icons.Default.TableChart,
                tag      = "DATA",
                gradient = listOf(InterColors.Orange, Color(0xFFCC4500)),
                onClick  = onNavigateToTables
            )

            // Botón Localidades
            ModuleCard(
                title    = "Localidades",
                subtitle = "Ciudades disponibles para recogida",
                icon     = Icons.Default.LocationOn,
                tag      = "GEO",
                gradient = listOf(Color(0xFF0077CC), Color(0xFF004A80)),
                onClick  = onNavigateToLocalities
            )
        }
    }
}

@Composable
private fun UserRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Icon(icon, null, tint = InterColors.Orange, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
            Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = InterColors.White)
        }
    }
}

@Composable
private fun ModuleCard(
    title: String, subtitle: String,
    icon: ImageVector, tag: String,
    gradient: List<Color>, onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(gradient)).padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) { Icon(icon, null, tint = Color.White, modifier = Modifier.size(24.dp)) }
                Spacer(Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(subtitle, color = Color.White.copy(alpha = 0.75f), style = MaterialTheme.typography.bodySmall)
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp)).background(Color.White.copy(alpha = 0.15f)).padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(tag, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                }
            }
        }
    }
}