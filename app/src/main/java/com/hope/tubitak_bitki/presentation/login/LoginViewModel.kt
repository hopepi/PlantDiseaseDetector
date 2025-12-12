package com.hope.tubitak_bitki.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when(event) {
            is LoginEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.email, error = null) }
            }
            is LoginEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password, error = null) }
            }
            LoginEvent.OnLoginClick -> {
                performLogin()
            }
            LoginEvent.OnRegisterClick -> {
            }
            LoginEvent.OnGoogleLoginClick -> {
            }
        }
    }

    private fun performLogin() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            delay(2000)

            if (_state.value.email.isNotEmpty() && _state.value.password.length > 5) {
                _state.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            } else {
                _state.update { it.copy(isLoading = false, error = "Email veya şifre hatalı!") }
            }
        }
    }
}