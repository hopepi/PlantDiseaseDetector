package com.hope.tubitak_bitki.presentation.home

data class HomeState(
    val userName: String = "Umut",
    val healthyPlantsCount: Int = 12,
    val diseasedPlantsCount: Int = 3,
    val recentScans: List<PlantItem> = emptyList(),
    val isLoading: Boolean = false
)


data class PlantItem(
    val id: Int,
    val name: String,
    val status: String,
    val date: String
)

sealed class HomeEvent {
    object OnScanClick : HomeEvent()
    data class OnPlantClick(val plantId: Int) : HomeEvent()
    object OnSeeAllHistoryClick : HomeEvent()
}