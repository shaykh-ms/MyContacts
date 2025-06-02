package com.example.mycontacts.presentation.contact_list_screen

sealed interface ContactEvent {
    data class AddContact(val name: String, val phone: String): ContactEvent
    data class EditContact(val id: String, val name: String, val phone: String): ContactEvent
    data object SyncContact: ContactEvent
}