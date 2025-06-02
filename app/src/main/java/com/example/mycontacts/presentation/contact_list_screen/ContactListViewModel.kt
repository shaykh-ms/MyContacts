package com.example.mycontacts.presentation.contact_list_screen

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mycontacts.domain.repository.ContactRepository
import com.example.mycontacts.util.ContactUtils
import com.example.mycontacts.util.Resource
import com.example.mycontacts.util.writeLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val contactUtil: ContactUtils,
    private val contactRepo: ContactRepository
) : ViewModel() {

    //var state by mutableStateOf(ContactState())

    private val _state = MutableStateFlow(ContactState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ContactUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun getLocalContacts() {
        val contacts = contactUtil.getAllContacts().sortedBy { it.name.lowercase() }
        writeLog("getLocalContacts", contacts.toString())
        _state.update {
            it.copy(
                isLoading = false,
                contacts = contacts.toMutableStateList()
            )
        }

    }


    fun onEvent(event: ContactEvent) {
        when (event) {
            is ContactEvent.AddContact -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        contactUtil.addContact(name = event.name, phone = event.phone)
                    }
                    getLocalContacts()
                    _uiEvent.emit(ContactUiEvent.ShowMessage("Contact added."))
                    _uiEvent.emit(ContactUiEvent.DismissDialog)
                }
            }

            ContactEvent.SyncContact -> {
                syncContact()

            }
            is ContactEvent.EditContact -> {
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        contactUtil.updateContact(event.id, event.name, event.phone)
                    }
                    getLocalContacts()
                    _uiEvent.emit(ContactUiEvent.ShowMessage("Contact updated."))
                    _uiEvent.emit(ContactUiEvent.DismissDialog)
                }
            }
        }
    }


    private fun syncContact() {
        viewModelScope.launch {
            contactRepo.getContactListing().collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }

                    is Resource.Loading -> {
                        _state.update {
                            it.copy(
                                isLoading = result.isLoading,
                            )
                        }
                    }

                    is Resource.Success -> {
                        try {
                            result.data?.let { listings ->
                                val localContacts = state.value.contacts

                                var newContactsAdded = false

                                listings.forEach { remoteContact ->
                                    val isPresent = localContacts.any {
                                        it.name.equals(remoteContact.name, ignoreCase = true) &&
                                                it.phone.replace("\\s".toRegex(), "") == remoteContact.phone.replace("\\s".toRegex(), "")
                                    }

                                    if (!isPresent) {
                                        contactUtil.addContact(
                                            remoteContact.name,
                                            remoteContact.phone
                                        )
                                        newContactsAdded = true
                                    }
                                }

                                if (newContactsAdded) {
                                    getLocalContacts()
                                    _uiEvent.emit(ContactUiEvent.ShowMessage("Contacts imported"))
                                } else {
                                    _uiEvent.emit(ContactUiEvent.ShowMessage("Contacts are already up to date"))
                                }
                            }
                        } catch (e: Exception) {
                            _uiEvent.emit(ContactUiEvent.ShowMessage("Error"))
                        }
                    }


                }
            }
        }
    }
}