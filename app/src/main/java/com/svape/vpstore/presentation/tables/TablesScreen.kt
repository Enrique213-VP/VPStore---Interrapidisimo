package com.svape.vpstore.presentation.tables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.svape.vpstore.domain.model.DataTable
import com.svape.vpstore.ui.theme.InterColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TablesScreen(
    onBack: () -> Unit,
    viewModel: TablesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { snackbarHostState.showSnackbar(it); viewModel.clearMessages() }
    }

    Scaffold(
        containerColor = InterColors.DarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Tablas del sistema", fontWeight = FontWeight.Bold)
                        Text("${state.tables.size} tablas en base de datos", style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = InterColors.White) } },
                actions = {
                    IconButton(onClick = { viewModel.sync() }) {
                        if (state.isSyncing)
                            CircularProgressIndicator(color = InterColors.Orange, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        else
                            Icon(Icons.Default.Sync, null, tint = InterColors.Orange)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InterColors.DarkSurface, titleContentColor = InterColors.White)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        if (state.error != null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Box(
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp))
                            .background(InterColors.RedError.copy(alpha = 0.1f))
                            .border(1.dp, InterColors.RedError.copy(alpha = 0.25f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lock, null, tint = InterColors.RedError, modifier = Modifier.size(40.dp))
                    }
                    Spacer(Modifier.height(20.dp))
                    Text(
                        "Sin acceso",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = InterColors.White
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No tienes permisos para ver esta información.\nComunícate con el administrador del sistema.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InterColors.GrayMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        lineHeight = 22.sp
                    )
                    Spacer(Modifier.height(28.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                            .background(InterColors.DarkSurface)
                            .border(1.dp, InterColors.DarkBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.SupportAgent, null, tint = InterColors.Orange, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Soporte Interrapidísimo", style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
                            Text("soporte@interrapidisimo.com", style = MaterialTheme.typography.bodySmall, color = InterColors.White, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        } else if (state.tables.isEmpty() && !state.isSyncing) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.TableChart, null, modifier = Modifier.size(60.dp), tint = InterColors.GrayDark)
                    Spacer(Modifier.height(12.dp))
                    Text("Sin tablas sincronizadas", color = InterColors.GrayMedium)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { viewModel.sync() }, colors = ButtonDefaults.buttonColors(containerColor = InterColors.Orange)) {
                        Icon(Icons.Default.Sync, null); Spacer(Modifier.width(6.dp)); Text("Sincronizar")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(state.tables) { TableCard(it) }
            }
        }
    }
}

@Composable
private fun TableCard(table: DataTable) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
            .background(InterColors.DarkSurface)
            .border(1.dp, InterColors.DarkBorder, RoundedCornerShape(14.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(InterColors.Orange.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) { Icon(Icons.Default.Storage, null, tint = InterColors.Orange, modifier = Modifier.size(22.dp)) }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(table.tableName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = InterColors.White)
            table.description?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = InterColors.GrayMedium) }
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                table.recordCount?.let { MiniTag("$it reg.", InterColors.BlueInfo) }
                MiniTag(if (table.isActive == true) "Activa" else "Inactiva", if (table.isActive == true) InterColors.GreenSuccess else InterColors.GrayMedium)
            }
        }
    }
}

@Composable
private fun MiniTag(label: String, color: Color) {
    Text(
        label,
        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(color.copy(alpha = 0.12f)).padding(horizontal = 8.dp, vertical = 3.dp),
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontWeight = FontWeight.SemiBold
    )
}