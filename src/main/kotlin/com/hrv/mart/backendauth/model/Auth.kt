package com.hrv.mart.backendauth.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("Auth")
data class Auth (
    @Id
    val email: String,
    val hashedPassword: String
)