package com.hope.tubitak_bitki.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hope.tubitak_bitki.presentation.ui.theme.BrightTeal
import com.hope.tubitak_bitki.presentation.ui.theme.ErrorRed
import com.hope.tubitak_bitki.presentation.ui.theme.NeonGreen

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                    )
                )
        ) {
            Box(modifier = Modifier.size(300.dp).offset((-100).dp, 100.dp).background(NeonGreen.copy(0.1f), CircleShape).blur(80.dp))
            Box(modifier = Modifier.size(200.dp).align(Alignment.TopEnd).offset(50.dp, (-50).dp).background(BrightTeal.copy(0.15f), CircleShape).blur(60.dp))
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Geçmiş Taramalar", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)

                IconButton(onClick = { viewModel.onEvent(HistoryEvent.OnDeleteAll) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Temizle", tint = Color.White.copy(0.6f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(
                    text = "Tümü",
                    selected = state.selectedFilter == HistoryFilter.ALL,
                    onClick = { viewModel.onEvent(HistoryEvent.OnFilterSelect(HistoryFilter.ALL)) }
                )
                FilterChip(
                    text = "Sağlıklı",
                    selected = state.selectedFilter == HistoryFilter.HEALTHY,
                    color = NeonGreen,
                    onClick = { viewModel.onEvent(HistoryEvent.OnFilterSelect(HistoryFilter.HEALTHY)) }
                )
                FilterChip(
                    text = "Hasta",
                    selected = state.selectedFilter == HistoryFilter.DISEASED,
                    color = ErrorRed,
                    onClick = { viewModel.onEvent(HistoryEvent.OnFilterSelect(HistoryFilter.DISEASED)) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.filteredPlants.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Henüz bir kayıt yok 🍃", color = Color.White.copy(0.5f))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp)
                ) {
                    items(state.filteredPlants) { item ->
                        HistoryItemCard(item)
                    }
                }
            }
        }
    }
}


@Composable
fun FilterChip(text: String, selected: Boolean, color: Color = NeonGreen, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) color else Color.White.copy(0.1f))
            .border(1.dp, if (selected) color else Color.White.copy(0.2f), RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text, color = if (selected) Color.Black else Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
    }
}

@Composable
fun HistoryItemCard(item: HistoryItem) {
    val isHealthy = item.status == "Healthy"
    val statusColor = if (isHealthy) NeonGreen else ErrorRed
    val statusText = if (isHealthy) "Sağlıklı" else "Hasta"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(0.4f))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(20.dp))
            .clickable { /* Detay */ }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Image, contentDescription = null, tint = Color.White.copy(0.3f), modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.date, color = Color.White.copy(0.6f), fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(if(isHealthy) Icons.Default.CheckCircle else Icons.Default.Warning, null, tint = statusColor, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("%${item.confidence} Doğruluk", color = statusColor, fontSize = 12.sp)
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusColor.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(statusText, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}