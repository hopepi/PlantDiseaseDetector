package com.hope.tubitak_bitki.presentation.profile

data class ProfileState(
    val fullName: String = "Umut",
    val email: String = "umut@tubitak.com",
    val profileImage: String? = null,
    val scanCount: Int = 15,
    val isProMember: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val language: String = "Türkçe",
    val isLoading: Boolean = false
)

sealed class ProfileEvent {
    object OnEditProfileClick : ProfileEvent()
    object OnToggleNotifications : ProfileEvent()
    object OnLanguageClick : ProfileEvent()
    object OnHelpClick : ProfileEvent()
    object OnLogoutClick : ProfileEvent()
}