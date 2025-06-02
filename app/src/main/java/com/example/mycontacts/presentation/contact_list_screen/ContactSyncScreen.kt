package com.example.mycontacts.presentation.contact_list_screen

import ContactSearchItem
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mycontacts.R
import com.example.mycontacts.domain.Contact
import com.example.mycontacts.presentation.contact_list_screen.components.ContactItem
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ContactSyncScreen(
    state: ContactState,
    event: SharedFlow<ContactUiEvent>,
    onEvent: (ContactEvent) -> Unit,
    viewModel: ContactListViewModel = hiltViewModel(),
    hasPermission: Boolean,
    onRequestPermissions: () -> Unit
) {

    val context = LocalContext.current

    var showAddScreen by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf<Contact?>(null) }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            viewModel.getLocalContacts()
        }
    }

    LaunchedEffect(Unit) {
        event.collectLatest { event ->
            when (event) {
                ContactUiEvent.DismissDialog -> {
                    showAddScreen = false
                    selectedContact = null
                }

                is ContactUiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    if (showAddScreen) {
        AddContactDialog(
            onDismiss = { showAddScreen = false },
            onAddContact = { name, phone ->
                onEvent(ContactEvent.AddContact(name, phone))
            }
        )
    }

    if (selectedContact != null) {
        EditContactDialog(
            contact = selectedContact!!,
            onDismiss = { selectedContact = null },
            onSave = { id, newName, newPhone ->
                onEvent(ContactEvent.EditContact(id, newName, newPhone))
            }
        )
    }




    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        colorResource(id = R.color.white),
                        colorResource(id = R.color.blue_0xFF87CEEB)
                    )
                )
            )
    ) {
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            ContactSearchItem(text = "", onValueChange = {})
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn {
                items(state.contacts) { contact ->
                    ContactItem(contact = contact, onClick = {
                        selectedContact = it
                    })
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            FloatingActionButton(
                onClick = {
                    if (hasPermission) {
                        onEvent(ContactEvent.SyncContact)
                    } else {
                        onRequestPermissions()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                containerColor = colorResource(R.color.fab_blue),
                contentColor = colorResource(R.color.white)

            ) {
                Icon(
                    imageVector = Icons.Default.Sync,
                    contentDescription = "Sync"
                )
            }

            FloatingActionButton(
                onClick = {
                    showAddScreen = true
                },
                shape = RoundedCornerShape(12.dp),
                containerColor = colorResource(R.color.fab_blue),
                contentColor = colorResource(R.color.white)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        }

        // Loader overlay at center
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = colorResource(id = R.color.fab_blue)
                )
            }
        }

    }

}
/*
@Composable
@Preview(
    showBackground = true,
)
fun ContactSyncScreenPreview() {

    ContactSyncScreen(
        state = ContactState(),
        event = MutableSharedFlow<ContactUiEvent>(),
        onEvent = {

        },
        hasPermission = true,
        onRequestPermissions = {},
        //viewModel =
    )
}*/






