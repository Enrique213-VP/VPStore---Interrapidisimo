package com.svape.vpstore.presentation.localities

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
import com.svape.vpstore.domain.model.Locality
import com.svape.vpstore.ui.theme.InterColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalitiesScreen(
    onBack: () -> Unit,
    viewModel: LocalitiesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = InterColors.DarkBg,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Localidades", fontWeight = FontWeight.Bold)
                        Text("${state.filtered.size} ciudades disponibles", style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
                    }
                },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = InterColors.White) } },
                actions = { IconButton(onClick = { viewModel.load() }) { Icon(Icons.Default.Refresh, null, tint = InterColors.Orange) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = InterColors.DarkSurface, titleContentColor = InterColors.White)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            OutlinedTextField(
                value = state.query,
                onValueChange = { viewModel.search(it) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                placeholder = { Text("Buscar ciudad o departamento...", color = InterColors.GrayMedium) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = InterColors.GrayMedium) },
                trailingIcon = {
                    if (state.query.isNotEmpty())
                        IconButton(onClick = { viewModel.search("") }) { Icon(Icons.Default.Clear, null, tint = InterColors.GrayMedium) }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = InterColors.Orange,
                    unfocusedBorderColor    = InterColors.DarkBorder,
                    focusedContainerColor   = InterColors.DarkSurface,
                    unfocusedContainerColor = InterColors.DarkSurface,
                    focusedTextColor        = InterColors.White,
                    unfocusedTextColor      = InterColors.White,
                    cursorColor             = InterColors.Orange
                ),
                singleLine = true
            )

            when {
                state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = InterColors.Orange)
                }
                state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                        Icon(Icons.Default.WifiOff, null, modifier = Modifier.size(52.dp), tint = InterColors.GrayDark)
                        Spacer(Modifier.height(10.dp))
                        Text("Error al cargar", fontWeight = FontWeight.Bold, color = InterColors.White)
                        Text(state.error!!, style = MaterialTheme.typography.bodySmall, color = InterColors.GrayMedium)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.load() }, colors = ButtonDefaults.buttonColors(containerColor = InterColors.Orange)) {
                            Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(6.dp)); Text("Reintentar")
                        }
                    }
                }
                state.filtered.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.SearchOff, null, modifier = Modifier.size(52.dp), tint = InterColors.GrayDark)
                        Spacer(Modifier.height(10.dp))
                        Text("Sin resultados para \"${state.query}\"", color = InterColors.GrayMedium)
                    }
                }
                else -> LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.filtered) { LocalityCard(it) }
                }
            }
        }
    }
}

@Composable
private fun LocalityCard(locality: Locality) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(InterColors.DarkSurface)
            .border(1.dp, InterColors.DarkBorder, RoundedCornerShape(12.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
                .background(InterColors.Orange.copy(alpha = 0.15f))
                .border(1.dp, InterColors.Orange.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                locality.cityCode,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = InterColors.Orange,
                letterSpacing = 1.sp
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(locality.fullName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = InterColors.White)
            locality.department?.let {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(11.dp), tint = InterColors.GrayMedium)
                    Spacer(Modifier.width(3.dp))
                    Text(it, style = MaterialTheme.typography.labelSmall, color = InterColors.GrayMedium)
                }
            }
        }
    }
}