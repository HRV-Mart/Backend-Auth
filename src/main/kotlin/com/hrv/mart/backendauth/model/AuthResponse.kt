package com.hrv.mart.backendauth.model

data class AuthResponse (
    val emailId: String,
    val userType: UserType
)