package com.example.firebasedatabase.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.firebasedatabase.data.util.AuthManager
import com.example.firebasedatabase.data.util.RealtimeManager
import com.example.firebasedatabase.domain.model.Contact
import com.example.firebasedatabase.presentation.ui.state.ContactState

class ContactsViewModel(
    private val realtimeDatabase: RealtimeManager,
    private val authManager: AuthManager
): ViewModel() {
    var contactState by mutableStateOf(ContactState())
        private set

    val currentUser = authManager.getCurrentUser()
    val contacts = realtimeDatabase.getContactsFlow()

    fun setName(name: String){
        contactState = contactState.copy(name = name)
    }

    fun setPhone(phone: String){
        contactState = contactState.copy(phone = phone)
    }

    fun setEmail(email: String){
        contactState = contactState.copy(email = email)
    }

    fun addContact(){
        realtimeDatabase.addContact(
            Contact(
                name = contactState.name,
                phone = contactState.phone,
                email = contactState.email,
                uid = currentUser?.uid.toString()
            )
        )
        contactState = ContactState()
    }

    fun deleteContact(contactId: String){
        realtimeDatabase.deleteContact(contactId)
    }
}