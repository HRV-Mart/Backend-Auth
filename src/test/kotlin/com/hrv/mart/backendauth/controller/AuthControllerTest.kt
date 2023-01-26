package com.hrv.mart.backendauth.controller

import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.service.AuthService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
import java.lang.Exception

class AuthControllerTest {
    private val mockAuthRepository = mock(AuthRepository::class.java)
    private val authService = AuthService(mockAuthRepository)
    private val authController = AuthController(authService)
    private val auth = Auth(
        email = "test@test.com",
        hashedPassword = "hashedPassword"
    )
    private final val response = mock(ServerHttpResponse::class.java)
    @Test
    fun `should sign up and save auth in database when it does not exist`() {
        doReturn(Mono.just(auth))
            .`when`(mockAuthRepository)
            .insert(auth)
        StepVerifier.create(authController.signUp(auth, response))
            .expectNext("Signup Successfully")
            .verifyComplete()
    }
    @Test
    fun `should not sign up when it auth exist in database`() {
        doReturn(Exception().toMono<Exception>())
            .`when`(mockAuthRepository)
            .insert(auth)
        StepVerifier.create(authController.signUp(auth, response))
            .expectNext("Auth already exist")
            .verifyComplete()
    }
}