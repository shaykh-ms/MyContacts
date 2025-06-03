package com.example.mycontacts.data.remote
import com.example.mycontacts.data.remote.dto.ContactResponse
import retrofit2.http.GET

interface ContactService {

    @GET("api/contacts")
    suspend fun getContactList(): ContactResponse

}