package com.example.mycontacts.presentation.contact_list_screen

import com.example.mycontacts.domain.Contact

sealed interface ContactUiEvent {
    data object DismissDialog: ContactUiEvent
    data class ShowMessage(val message: String): ContactUiEvent

}