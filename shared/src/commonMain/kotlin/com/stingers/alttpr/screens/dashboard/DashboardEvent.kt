package com.stingers.alttpr.screens.dashboard

import com.stingers.alttpr.navigation.Screen

sealed interface DashboardEvent {
    object GenerateRandom : DashboardEvent
    object RefreshData : DashboardEvent
    data class NavigateTo(val value: Screen) : DashboardEvent
}
