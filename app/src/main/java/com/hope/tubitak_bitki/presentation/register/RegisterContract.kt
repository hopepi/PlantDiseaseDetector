package com.hope.tubitak_bitki.presentation.register

data class RegisterState(
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccessful: Boolean = false
)

sealed class RegisterEvent {
    data class OnNameChange(val name: String) : RegisterEvent()
    data class OnSurnameChange(val surname: String) : RegisterEvent()
    data class OnEmailChange(val email: String) : RegisterEvent()
    data class OnPasswordChange(val password: String) : RegisterEvent()
    data class OnConfirmPasswordChange(val confirmPassword: String) : RegisterEvent()
    object OnRegisterClick : RegisterEvent()
}