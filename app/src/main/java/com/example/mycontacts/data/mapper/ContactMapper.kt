package com.example.mycontacts.data.mapper

import com.example.mycontacts.data.remote.dto.ContactDto
import com.example.mycontacts.domain.Contact

fun ContactDto.toContact(): Contact {
    return Contact(
        id = this.id,
        name = this.fullName,
        phone = this.phone,
    )
}
