package com.hope.tubitak_bitki.presentation.register

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
class RegisterViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when(event) {
            is RegisterEvent.OnNameChange -> _state.update { it.copy(name = event.name, error = null) }
            is RegisterEvent.OnSurnameChange -> _state.update { it.copy(surname = event.surname, error = null) }
            is RegisterEvent.OnEmailChange -> _state.update { it.copy(email = event.email, error = null) }
            is RegisterEvent.OnPasswordChange -> _state.update { it.copy(password = event.password, error = null) }
            is RegisterEvent.OnConfirmPasswordChange -> _state.update { it.copy(confirmPassword = event.confirmPassword, error = null) }
            RegisterEvent.OnRegisterClick -> performRegister()
        }
    }

    private fun performRegister() {
        val currentState = _state.value

        if (currentState.name.isBlank()) {
            _state.update { it.copy(error = "Ad alanı boş bırakılamaz.") }
            return
        }

        if (currentState.surname.isBlank()) {
            _state.update { it.copy(error = "Soyad alanı boş bırakılamaz.") }
            return
        }

        if (currentState.email.isBlank()) {
            _state.update { it.copy(error = "Email alanı boş bırakılamaz.") }
            return
        }

        if (currentState.password.length < 6) {
            _state.update { it.copy(error = "Şifre en az 6 karakter olmalı.") }
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            _state.update { it.copy(error = "Şifreler eşleşmiyor!") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            delay(2000)
            _state.update { it.copy(isLoading = false, isRegisterSuccessful = true) }
        }
    }
}