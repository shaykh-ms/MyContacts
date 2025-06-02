package com.example.mycontacts.domain.repository

import com.example.mycontacts.domain.Contact
import com.example.mycontacts.util.Resource
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    suspend fun getContactListing(): Flow<Resource<List<Contact>>>
}