package com.example.mycontacts.presentation.contact_list_screen

sealed interface ContactUiEvent {
    data object DismissDialog: ContactUiEvent
    data class ShowMessage(val message: String): ContactUiEvent
}