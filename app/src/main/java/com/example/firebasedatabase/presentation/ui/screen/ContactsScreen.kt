package com.example.firebasedatabase.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.example.firebasedatabase.data.util.AuthManager
import com.example.firebasedatabase.domain.model.Contact
import com.example.firebasedatabase.data.util.RealtimeManager
import com.example.firebasedatabase.presentation.ui.state.Route
import com.example.firebasedatabase.presentation.viewmodel.ContactsViewModel

@Composable
fun ContactsScreen(
    viewModel: ContactsViewModel
) {
    val contacts by viewModel.contacts.collectAsState(emptyList())
    var showAddContactDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddContactDialog = true },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }

            if (showAddContactDialog) {
                AddContactDialog(
                    onContactAdded = {
                         viewModel.addContact()
                         showAddContactDialog = false
                    },
                    onDialogDismiss = { showAddContactDialog = false },
                    viewModel = viewModel
                )
            }
        }
    ) { innerPadding ->
        if (contacts.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                contacts.forEach { contact ->
                    item{
                        ContactItem(
                            contact = contact,
                            onDeleteContact = {
                                viewModel.deleteContact(contact.key ?: "")
                            }
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No contacts available",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Thin,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ContactItem(
    modifier: Modifier = Modifier,
    contact: Contact,
    onDeleteContact: (Contact) -> Unit = {},
) {
    var showDeleteContactDialog by rememberSaveable { mutableStateOf(false) }


    if(showDeleteContactDialog){
        DeleteContactDialog(
            onConfirm = {
                onDeleteContact(contact)
                showDeleteContactDialog = false
            },
            onDismiss = { showDeleteContactDialog = false }
        )
    }

    Card(
        modifier = modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 0.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = contact.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.phone,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contact.email,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Thin,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }
            Row (
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center
            ){
                IconButton(onClick = { showDeleteContactDialog = true }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
                }
            }
        }
    }
}

@Composable
fun AddContactDialog(
    modifier: Modifier = Modifier,
    onContactAdded: () -> Unit,
    onDialogDismiss: () -> Unit,
    viewModel: ContactsViewModel
) {
    val contactState = viewModel.contactState

    AlertDialog(
        title = { Text(text = "Add Contact") },
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = onContactAdded) {
                Text(text = "Add")
            }
        },
        dismissButton = {
            Button(onClick = onDialogDismiss) {
                Text(text = "Cancel")
            }
        },
        text = {
            Column {
                TextField(
                    value = contactState.name,
                    onValueChange = { viewModel.setName(it)},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = contactState.phone,
                    onValueChange = { viewModel.setPhone(it)},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    label = { Text(text = "Phone Number") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = contactState.email,
                    onValueChange = { viewModel.setEmail(it)},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    label = { Text(text = "Email") }
                )
            }
        }
    )

}

@Composable
fun DeleteContactDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Delete Contact") },
        text = { Text(text = "Are you sure you want to delete this contact?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Preview
@Composable
fun CustomComposable(){
}