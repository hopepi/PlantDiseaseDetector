package com.hope.tubitak_bitki.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
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
import com.hope.tubitak_bitki.presentation.ui.theme.BrightTeal
import com.hope.tubitak_bitki.presentation.ui.theme.ErrorRed
import com.hope.tubitak_bitki.presentation.ui.theme.NeonGreen

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
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
            Box(modifier = Modifier.size(300.dp).offset(100.dp, (-50).dp).background(NeonGreen.copy(0.1f), CircleShape).blur(80.dp))
            Box(modifier = Modifier.size(250.dp).align(Alignment.BottomStart).offset((-50).dp, 50.dp).background(BrightTeal.copy(0.15f), CircleShape).blur(60.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(2.dp, Brush.linearGradient(listOf(NeonGreen, BrightTeal)), CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White.copy(0.8f),
                    modifier = Modifier.size(60.dp)
                )
                if (state.isProMember) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(NeonGreen, CircleShape)
                            .padding(6.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.Black, modifier = Modifier.size(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(state.fullName, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(state.email, color = Color.White.copy(0.6f), fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White.copy(0.1f))
                    .clickable { viewModel.onEvent(ProfileEvent.OnEditProfileClick) }
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text("Profili Düzenle", color = NeonGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProfileStatCard(modifier = Modifier.weight(1f), title = "Taramalar", value = "${state.scanCount}", icon = Icons.Default.QrCodeScanner)
                ProfileStatCard(modifier = Modifier.weight(1f), title = "Üyelik", value = if(state.isProMember) "PRO" else "Free", icon = Icons.Default.WorkspacePremium, iconColor = if(state.isProMember) NeonGreen else Color.Gray)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Ayarlar", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.Black.copy(0.4f))
                    .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(20.dp))
            ) {
                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Bildirimler",
                    trailing = {
                        Switch(
                            checked = state.isNotificationEnabled,
                            onCheckedChange = { viewModel.onEvent(ProfileEvent.OnToggleNotifications) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonGreen,
                                checkedTrackColor = NeonGreen.copy(0.3f),
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.Gray
                            )
                        )
                    }
                )
                HorizontalDivider(color = Color.White.copy(0.1f), thickness = 1.dp)

                ProfileMenuItem(
                    icon = Icons.Default.Language,
                    title = "Dil Seçeneği",
                    trailing = {
                        Text(state.language, color = Color.White.copy(0.6f), fontSize = 14.sp)
                    },
                    onClick = { viewModel.onEvent(ProfileEvent.OnLanguageClick) }
                )
                HorizontalDivider(color = Color.White.copy(0.1f), thickness = 1.dp)

                ProfileMenuItem(
                    icon = Icons.Default.HelpOutline,
                    title = "Yardım & Destek",
                    onClick = { viewModel.onEvent(ProfileEvent.OnHelpClick) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.onEvent(ProfileEvent.OnLogoutClick)
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed.copy(alpha = 0.2f), contentColor = ErrorRed),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(0.5f))
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Çıkış Yap", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}


@Composable
fun ProfileStatCard(modifier: Modifier = Modifier, title: String, value: String, icon: ImageVector, iconColor: Color = NeonGreen) {
    Box(
        modifier = modifier
            .height(90.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(0.4f))
            .border(1.dp, Color.White.copy(0.1f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).background(iconColor.copy(0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(title, color = Color.White.copy(0.6f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = Color.White.copy(0.8f), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, color = Color.White, fontSize = 16.sp)
        }
        if (trailing != null) {
            trailing()
        } else {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.White.copy(0.4f))
        }
    }
}