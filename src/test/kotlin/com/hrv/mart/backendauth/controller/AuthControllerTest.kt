package com.hrv.mart.backendauth.controller

import com.hrv.mart.authlibrary.model.AppWriteAuth
import com.hrv.mart.authlibrary.model.AuthRequest
import com.hrv.mart.authlibrary.model.AuthWithUserType
import com.hrv.mart.authlibrary.model.UserType
import com.hrv.mart.backendauth.repository.AppWriteAuthRepository
import com.hrv.mart.backendauth.repository.AuthWithUserTypeRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import com.hrv.mart.backendauth.service.AuthService
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import reactor.test.StepVerifier
import java.util.*

class AuthControllerTest {
    private val mockAppWriteAuthRepository = mock(AppWriteAuthRepository::class.java)
    private val mockKafkaRepository = mock(KafkaRepository::class.java)
    private val mockAuthWithUserTypeRepository = mock(AuthWithUserTypeRepository::class.java)
    private val response = mock(ServerHttpResponse::class.java)

    private val authService = AuthService(
        mockAppWriteAuthRepository,
        mockAuthWithUserTypeRepository,
        mockKafkaRepository
    )
    private val authController = AuthController(authService)

    @Test
    fun `should return login successful message if jwt is valid`(): Unit = runBlocking {
        val jwt = "A_VALID_JWT"
        val userType = UserType.USER

        val auth = AppWriteAuth(
            userId = UUID.randomUUID().toString(),
            email = "test@test.com",
            emailVerification = true,
            createdAt = Date().toString(),
            updatedAt = Date().toString(),
            name = "Test User"
        )
        val authWithUserType = AuthWithUserType(
            userId = auth.userId,
            userType = userType
        )
        doReturn(Mono.just(auth))
            .`when`(mockAppWriteAuthRepository)
            .getAuthAccount(jwt)
        doReturn(Mono.just(authWithUserType))
            .`when`(mockAuthWithUserTypeRepository)
            .findByUserId(auth.userId)
        doReturn(Mono.just(true))
            .`when`(mockAuthWithUserTypeRepository)
            .existsByUserId(auth.userId)
        doReturn(Mono.empty<SenderResult<Void>>())
            .`when`(mockKafkaRepository)
            .createUser(auth.toUser())

        StepVerifier
            .create(
                authController.getInfoFromJWT(
                    AuthRequest(jwt, userType),
                    response
                )
            )
            .expectNext(auth)
            .verifyComplete()
    }
    @Test
    fun `should return unable to login message if jwt is invalid`(): Unit = runBlocking {
        val jwt = "A_INVALID_JWT"
        val userType = UserType.USER


        doReturn(Mono.error<AppwriteException>(AppwriteException("JWT is invalid")))
            .`when`(mockAppWriteAuthRepository)
            .getAuthAccount(jwt)

        StepVerifier
            .create(
                authController.getInfoFromJWT(
                    AuthRequest(jwt, userType),
                    response
                )
            )
            .expectComplete()
            .verify()
    }
}