package com.hrv.mart.backendauth.model

import com.hrv.mart.userlibrary.model.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

data class Auth (
    val createdAt: String,
    val updatedAt: String,
    val name: String,
    val email: String,
    val emailVerification: Boolean
) {
    fun toUser() =
        User(
            emailId = email,
            name = name
        )
}
