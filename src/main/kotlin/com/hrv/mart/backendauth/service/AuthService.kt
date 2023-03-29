package com.hrv.mart.backendauth.service

import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import com.hrv.mart.userlibrary.model.User
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
    fun login(auth: Auth, response: ServerHttpResponse) =
        authRepository.existsAuthByEmailAndHashedPassword(auth.email, auth.hashedPassword)
            .flatMap { exist ->
                if (exist) {
                    response.statusCode = HttpStatus.OK
                    return@flatMap Mono.just("Login Successfully")
                }
                else {
                    response.statusCode = HttpStatus.NOT_FOUND
                    return@flatMap Mono.just("Auth Not Found")
                }
            }
    fun signUp(auth: Auth, user: User, response: ServerHttpResponse): Mono<String> =
        authRepository.insert(auth)
            .flatMap {
                response.statusCode = HttpStatus.OK
                kafkaRepository
                    .createUser(user)
                    .then(
                        Mono.just("Signup Successfully")
                    )
            }
            .onErrorResume {
                response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
                Mono.just("Auth already exist")
            }
    fun updatePassword(auth: Auth, response: ServerHttpResponse) =
        authRepository.existsById(auth.email)
            .flatMap {
                if (it) {
                    response.statusCode = HttpStatus.OK
                    authRepository.save(auth)
                        .then(Mono.just("Password Updated Successfully"))
                }
                else {
                    response.statusCode = HttpStatus.NOT_FOUND
                    Mono.just("Auth Not Found")
                }
            }
    fun deleteAuth(emailId: String, response: ServerHttpResponse) =
        authRepository.existsById(emailId)
            .flatMap { exist ->
                response.statusCode = HttpStatus.OK
                if (exist) {
                    kafkaRepository
                        .deleteUser(emailId)
                        .then (
                            authRepository.deleteById(emailId)
                                .then(Mono.just("Auth Deleted Successfully"))
                        )
                }
                else {
                    response.statusCode = HttpStatus.NOT_FOUND
                    Mono.just("Auth Not Found")
                }
            }
}
