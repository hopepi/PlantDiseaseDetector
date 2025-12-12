package com.hope.tubitak_bitki.presentation.history

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        // Mock Data (Home'dan bağımsız, kendi verisi)
        val mockData = listOf(
            HistoryItem(1, "Monstera Deliciosa", "12 Ara, 14:30", "Healthy", 98),
            HistoryItem(2, "Orkide", "11 Ara, 09:15", "Diseased", 85),
            HistoryItem(3, "Kaktüs", "10 Ara, 16:45", "Healthy", 99),
            HistoryItem(4, "Paşa Kılıcı", "09 Ara, 11:20", "Healthy", 92),
            HistoryItem(5, "Gül", "05 Ara, 08:30", "Diseased", 78),
            HistoryItem(6, "Aloe Vera", "01 Ara, 10:00", "Healthy", 95)
        )

        _state.update {
            it.copy(
                plants = mockData,
                filteredPlants = mockData
            )
        }
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.OnFilterSelect -> {
                _state.update { currentState ->
                    val filtered = when (event.filter) {
                        HistoryFilter.ALL -> currentState.plants
                        HistoryFilter.HEALTHY -> currentState.plants.filter { it.status == "Healthy" }
                        HistoryFilter.DISEASED -> currentState.plants.filter { it.status == "Diseased" }
                    }
                    currentState.copy(selectedFilter = event.filter, filteredPlants = filtered)
                }
            }
            is HistoryEvent.OnItemClick -> {
            }
            HistoryEvent.OnDeleteAll -> {
                _state.update { it.copy(plants = emptyList(), filteredPlants = emptyList()) }
            }
        }
    }
}