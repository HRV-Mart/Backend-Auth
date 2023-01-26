package com.hrv.mart.backendauth.repository

import com.hrv.mart.backendauth.model.Auth
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AuthRepository: ReactiveMongoRepository<Auth, String> {
    fun existsAuthByEmailAndHashedPassword(email: String, hashedPassword: String): Mono<Boolean>
}