package com.hrv.mart.backendauth.service

import com.hrv.mart.backendauth.model.UserType
import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import io.appwrite.exceptions.AppwriteException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service

@Service
class AuthService (
    @Autowired
    private val authRepository: AuthRepository,
    @Autowired
    private val kafkaRepository: KafkaRepository
)
{
    suspend fun clientRequest(
        jwt: String,
        userType: UserType,
        response: ServerHttpResponse
    ): String {
        return try {
            val auth = authRepository.getAuthAccount(jwt)
            kafkaRepository
                .createUser(auth.toUser())
                .block()
            response.statusCode = HttpStatus.OK
            "Login Successful"

        } catch (_: AppwriteException) {
            response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            "Unable to login"
        }
    }
}
