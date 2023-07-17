package com.hrv.mart.backendauth.service

import com.hrv.mart.authlibrary.model.AppWriteAuth
import com.hrv.mart.authlibrary.model.AuthWithUserType
import com.hrv.mart.authlibrary.model.UserType
import com.hrv.mart.backendauth.repository.AppWriteAuthRepository
import com.hrv.mart.backendauth.repository.AuthWithUserTypeRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthService (
    @Autowired
    private val appWriteAuthRepository: AppWriteAuthRepository,
    @Autowired
    private val authWithUserTypeRepository: AuthWithUserTypeRepository,
    @Autowired
    private val kafkaRepository: KafkaRepository
)
{
    fun clientRequest(
        jwt: String,
        userType: UserType,
        response: ServerHttpResponse
    ) =
        appWriteAuthRepository
            .getAuthAccount(jwt)
            .flatMap {
                insertUserType(it.userId)
                    .then(Mono.just(it))
            }
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
    private fun insertUserType(userId: String) =
        authWithUserTypeRepository
            .existsByUserId(userId)
            .flatMap {
                if (it) {
                    Mono.empty()
                }
                else {
                    authWithUserTypeRepository
                        .insert(
                            AuthWithUserType(
                                userId = userId,
                                userType = UserType.USER
                            )
                        )
                }
            }
}
