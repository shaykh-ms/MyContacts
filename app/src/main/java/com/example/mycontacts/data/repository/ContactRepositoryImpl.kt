package com.example.mycontacts.data.repository

import android.net.http.HttpException
import com.example.mycontacts.data.mapper.toContact
import com.example.mycontacts.data.remote.ContactService
import com.example.mycontacts.domain.Contact
import com.example.mycontacts.domain.repository.ContactRepository
import com.example.mycontacts.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactApi: ContactService
) : ContactRepository {
    override suspend fun getContactListing(): Flow<Resource<List<Contact>>> = flow {
        emit(Resource.Loading(true))

        try {
            val response = contactApi.getContactList()
            if (response.success) {
                val contacts = response.Data.users.map { it.toContact() }
                emit(Resource.Success(contacts))
            } else {
                emit(Resource.Error("Failed to fetch contacts"))
            }
        } catch (e: IOException) {
            emit(Resource.Error("Network error: ${e.localizedMessage ?: "Unknown error"}"))
        } catch (e: Exception) {
            emit(Resource.Error("Unexpected error: ${e.localizedMessage ?: "Unknown error"}"))
        } finally {
            emit(Resource.Loading(false))
        }
    }
}
