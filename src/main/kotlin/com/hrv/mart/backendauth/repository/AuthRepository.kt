package com.hrv.mart.backendauth.repository

import com.hrv.mart.backendauth.model.Auth
import io.appwrite.Client
import io.appwrite.services.Account
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class AuthRepository (
    @Autowired
    private val client: Client
)
{
    suspend fun getAuthAccount(jwt: String): Auth {
        client.setJWT(jwt)
        val account = Account(client)
        val details = account.get()
        return Auth(
            name = details.name,
            email = details.email,
            emailVerification = details.emailVerification,
            createdAt = details.createdAt,
            updatedAt = details.updatedAt
        )
    }
}
