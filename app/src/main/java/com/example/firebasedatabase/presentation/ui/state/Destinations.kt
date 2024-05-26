package com.example.firebasedatabase.presentation.ui.state

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Contacts: Route

    @Serializable
    data class Notes(val title: String) : Route

    @Serializable
    data object Login : Route
}