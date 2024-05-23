package com.example.firebasedatabase.data.util

import android.content.Context
import com.example.firebasedatabase.domain.model.Contact
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeManager(
    context: Context
) {
    private val databaseReference = FirebaseDatabase.getInstance().reference.child("contacts")
    private val authManager = AuthManager(context)

    fun addContact(contact: Contact){
        val key = databaseReference.push().key

        if(key != null){
            databaseReference.child(key).setValue(contact)
        }
    }
    fun deleteContact(contactId: String){
        databaseReference.child(contactId).removeValue()
    }
    fun updateContact(contactId: String, updatedContact: Contact){
        databaseReference.child(contactId).setValue(updatedContact)
    }
    fun getContacts(): Flow<List<Contact>> {
        val id = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            val listener = databaseReference.addValueEventListener( object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val contacts = snapshot.children.mapNotNull { snapshot ->
                        //cast snapshot children to contact
                        val contact = snapshot.getValue(Contact::class.java)

                        //copy keys from snapshot
                        snapshot.key?.let { key -> contact?.copy(key = key) }
                    }
                    //only return to the flow, contacts that belong to the current user
                    trySend(contacts.filter { it.uid == id}).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    //close the flow channel if there is an error
                    close(error.toException())
                }
            })
            awaitClose {
                databaseReference.removeEventListener(listener)
            }
        }
    return flow
    }
}