package com.hrv.mart.backendauth.service

import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.model.UserType
import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import io.appwrite.exceptions.AppwriteException
import okhttp3.internal.wait
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService (
    @Autowired
    private val authRepository: AuthRepository,
    @Autowired
    private val kafkaRepository: KafkaRepository
)
{
    fun clientRequest(
        jwt: String,
        userType: UserType,
        response: ServerHttpResponse
    ): Mono<Auth> {
        userType.name
        return authRepository.getAuthAccount(jwt)
            .flatMap {auth ->
                response.statusCode = HttpStatus.OK
                kafkaRepository
                    .createUser(auth.toUser())
                    .then(Mono.just(auth))
            }
            .onErrorResume {
                response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                Mono.empty()
            }
    }
}
