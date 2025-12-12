package com.hope.tubitak_bitki.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val mockPlants = listOf(
                PlantItem(1, "Monstera", "Healthy", "Bugün, 14:30"),
                PlantItem(2, "Orkide", "Diseased", "Dün, 09:15"),
                PlantItem(3, "Kaktüs", "Healthy", "12 Ara"),
                PlantItem(4, "Potos", "Healthy", "10 Ara")
            )

            _state.update {
                it.copy(
                    recentScans = mockPlants,
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: HomeEvent) {
    }
}