package com.hrv.mart.backendauth.model

import com.hrv.mart.userlibrary.User

data class UserDetail (
    val authDetail: Auth,
    val userDetail: User
)