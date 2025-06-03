package com.example.mycontacts.data.remote.dto

data class ContactResponse(
    val success: Boolean,
    val Data: Data
)

data class Data(
    val date: String?,
    val totalUsers: Int?,
    val users: List<ContactDto?>?
)

data class ContactDto(
    val id: String?,
    val fullName: String?,
    val phone: String?,
    val email: String?,
    val course: String?,
    val enrolledOn: String?
)

