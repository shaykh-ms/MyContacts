package com.example.mycontacts.presentation.contact_list_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.mycontacts.domain.Contact

data class ContactState(
    val contacts: SnapshotStateList<Contact> = mutableStateListOf(),
    val clickedContact: Contact? = null,
    val isLoading: Boolean = false,
    val query: String = "",
    val error: String? = null
)
