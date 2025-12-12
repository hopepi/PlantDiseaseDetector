package com.hope.tubitak_bitki.presentation.history

data class HistoryItem(
    val id: Int,
    val name: String,
    val date: String,
    val status: String,
    val confidence: Int,
    val imagePath: String? = null
)

data class HistoryState(
    val plants: List<HistoryItem> = emptyList(),
    val filteredPlants: List<HistoryItem> = emptyList(),
    val selectedFilter: HistoryFilter = HistoryFilter.ALL,
    val isLoading: Boolean = false
)

enum class HistoryFilter {
    ALL, HEALTHY, DISEASED
}

sealed class HistoryEvent {
    data class OnFilterSelect(val filter: HistoryFilter) : HistoryEvent()
    data class OnItemClick(val id: Int) : HistoryEvent()
    object OnDeleteAll : HistoryEvent()
}