package com.example.firebasedatabase.presentation.ui.state

import kotlinx.serialization.Serializable

@Serializable
object ContactsDestination

@Serializable
data class NotesDestination(
    val name: String
)