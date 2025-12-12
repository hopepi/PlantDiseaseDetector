package com.hope.tubitak_bitki.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.hope.tubitak_bitki.presentation.ui.theme.*

@Composable
fun HomeScreen(
    onCameraClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
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
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset((-50).dp, (-50).dp)
                    .background(NeonGreen.copy(0.1f), CircleShape)
                    .blur(60.dp)
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.BottomEnd)
                    .offset(50.dp, 50.dp)
                    .background(BrightTeal.copy(0.15f), CircleShape)
                    .blur(50.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(48.dp)) // Status bar boşluğu

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Merhaba, ${state.userName} 👋",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Bitkilerin bugün nasıl?",
                        color = Color.White.copy(0.7f),
                        fontSize = 14.sp
                    )
                }
                GlassIconButton(icon = Icons.Outlined.Notifications)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Kartı biraz yükselttim ferah olsun
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(NeonGreen.copy(0.15f), BrightTeal.copy(0.05f))
                        )
                    )
                    .border(
                        1.dp,
                        Brush.linearGradient(listOf(NeonGreen.copy(0.5f), Color.Transparent)),
                        RoundedCornerShape(24.dp)
                    )
                    .clickable { onCameraClick() } // Kartın tamamı da tıklanabilir olsun
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Yapay Zeka Taraması",
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp // Başlığı büyüttüm
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bitkini tara, hastalığı anında tespit edelim.",
                            color = Color.White.copy(0.8f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = onCameraClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonGreen,
                                contentColor = DarkForest // Yazı koyu olsun, okunur
                            ),
                            shape = RoundedCornerShape(50),
                            elevation = ButtonDefaults.buttonElevation(8.dp), // Gölge verdim
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                            modifier = Modifier.height(45.dp)
                        ) {
                            Text(
                                text = "Şimdi Tara",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(Color.White.copy(0.1f), CircleShape)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Sağlıklı",
                    count = state.healthyPlantsCount.toString(),
                    icon = Icons.Default.LocalFlorist,
                    color = NeonGreen
                )
                Spacer(modifier = Modifier.width(16.dp))
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Hasta",
                    count = state.diseasedPlantsCount.toString(),
                    icon = Icons.Default.Warning,
                    color = ErrorRed
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Son Taramalar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tümünü Gör",
                    color = NeonGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {}
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(state.recentScans) { plant ->
                    PlantHistoryCard(plant)
                }
            }
        }
    }
}


@Composable
fun GlassIconButton(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(Color.White.copy(0.1f))
            .border(1.dp, Color.White.copy(0.2f), CircleShape)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, count: String, icon: ImageVector, color: Color) {
    Box(
        modifier = modifier
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(0.4f))
            .border(1.dp, color.copy(0.3f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Column {
                Text(count, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(title, color = Color.White.copy(0.6f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PlantHistoryCard(plant: PlantItem) {
    val isHealthy = plant.status == "Healthy"
    val statusColor = if (isHealthy) NeonGreen else ErrorRed

    Box(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(0.05f))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(20.dp))
            .clickable { }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = Color.White.copy(0.3f), modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(plant.name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(statusColor))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if(isHealthy) "Sağlıklı" else "Hasta", color = statusColor, fontSize = 11.sp)
            }
        }
    }
}