package com.example.firebasedatabase.presentation.ui.state

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Contacts: Route

    @Serializable
    data object Notes : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object Home : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data object ForgotPassword :Route
}