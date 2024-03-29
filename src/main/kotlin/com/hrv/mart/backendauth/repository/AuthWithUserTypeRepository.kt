package com.hrv.mart.backendauth.repository

import com.hrv.mart.authlibrary.model.AuthWithUserType
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface AuthWithUserTypeRepository : ReactiveMongoRepository<AuthWithUserType, String> {
    fun existsByUserId(userId: String): Mono<Boolean>
    fun findByUserId(userId: String): Mono<AuthWithUserType>
}