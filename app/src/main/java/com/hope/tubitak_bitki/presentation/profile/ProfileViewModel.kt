package com.hope.tubitak_bitki.presentation.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnToggleNotifications -> {
                _state.update { it.copy(isNotificationEnabled = !it.isNotificationEnabled) }
            }
            ProfileEvent.OnLogoutClick -> {
            }
            else -> {
            }
        }
    }
}