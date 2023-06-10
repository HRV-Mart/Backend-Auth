package com.hrv.mart.backendauth.repository

import com.hrv.mart.backendauth.model.Auth
import io.appwrite.Client
import io.appwrite.services.Account
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Repository
class AuthRepository (
    @Autowired
    private val client: Client
)
{
    fun getAuthAccount(jwt: String): Mono<Auth> {
        client.setJWT(jwt)
        val account = Account(client)
        return runBlocking{account.get()}
            .toMono()
            .map {details ->
                Auth(
                    name = details.name,
                    email = details.email,
                    emailVerification = details.emailVerification,
                    createdAt = details.createdAt,
                    updatedAt = details.updatedAt
                )
            }

    }
}
