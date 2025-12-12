package com.hope.tubitak_bitki.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hope.tubitak_bitki.presentation.ui.theme.*

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isRegisterSuccessful) {
        if (state.isRegisterSuccessful) {
            onRegisterSuccess()
        }
    }

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
                    .fillMaxSize()
                    .imePadding(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth()
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                        .border(1.dp, Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.3f), Color.Transparent)), RoundedCornerShape(24.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Aramıza Katıl 🌱", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))

                    GlassTextField(
                        value = state.name,
                        onValueChange = { viewModel.onEvent(RegisterEvent.OnNameChange(it)) },
                        hint = "Ad",
                        icon = Icons.Default.Person
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GlassTextField(
                        value = state.email,
                        onValueChange = { viewModel.onEvent(RegisterEvent.OnEmailChange(it)) },
                        hint = "Email",
                        icon = Icons.Default.Email
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GlassTextField(
                        value = state.password,
                        onValueChange = { viewModel.onEvent(RegisterEvent.OnPasswordChange(it)) },
                        hint = "Şifre",
                        icon = Icons.Default.Lock,
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GlassTextField(
                        value = state.confirmPassword,
                        onValueChange = { viewModel.onEvent(RegisterEvent.OnConfirmPasswordChange(it)) },
                        hint = "Şifre Tekrar",
                        icon = Icons.Default.Lock,
                        isPassword = true
                    )

                    if (state.error != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = state.error!!, color = ErrorRed, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.onEvent(RegisterEvent.OnRegisterClick) },
                        modifier = Modifier.fillMaxWidth().height(50.dp).background(Brush.horizontalGradient(listOf(NeonGreen, BrightTeal)), RoundedCornerShape(12.dp)),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        if (state.isLoading) CircularProgressIndicator(color = Color.White) else Text("HESAP OLUŞTUR", color = DarkForest, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Zaten hesabın var mı? ", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
                        Text("Giriş Yap", color = NeonGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.clickable { onBackToLogin() })
                    }
                }
            }
        }
    }
}

@Composable
private fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    icon: ImageVector,
    isPassword: Boolean = false
) {
    var isVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(hint, color = Color.White.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = NeonGreen) },
        trailingIcon = if (isPassword) {
            { IconButton(onClick = { isVisible = !isVisible }) { Icon(if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, null, tint = Color.White.copy(alpha = 0.7f)) } }
        } else null,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = NeonGreen,
            unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
            focusedContainerColor = Color.Black.copy(alpha = 0.3f),
            unfocusedContainerColor = Color.Black.copy(alpha = 0.3f),
            cursorColor = NeonGreen,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        visualTransformation = if (isPassword && !isVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text, imeAction = ImeAction.Next)
    )
}