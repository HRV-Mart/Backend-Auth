package com.hrv.mart.backendauth.controller

import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.model.UserDetail
import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import com.hrv.mart.backendauth.service.AuthService
import com.hrv.mart.userlibrary.model.User
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
import java.lang.Exception

class AuthControllerTest {
    private val mockAuthRepository = mock(AuthRepository::class.java)
    private val mockKafkaRepository = mock(KafkaRepository::class.java)
    private val authService = AuthService(mockAuthRepository, mockKafkaRepository)
    private val authController = AuthController(authService)
    private val auth = Auth(
        email = "test@test.com",
        hashedPassword = "hashedPassword"
    )
    private val userDetail = UserDetail(
        userDetail = User(
            emailId = auth.email,
            name = "test"
        ),
        authDetail = auth
    )
    private val response = mock(ServerHttpResponse::class.java)
    @Test
    fun `should sign up and save auth in database when it does not exist`() {
        doReturn(Mono.just(auth))
            .`when`(mockAuthRepository)
            .insert(auth)
        doReturn(Mono.empty<SenderResult<Void>>())
            .`when`(mockKafkaRepository)
            .createUser(user = userDetail.userDetail)
        StepVerifier.create(authController.signUp(userDetail, response))
            .expectNext("Signup Successfully")
            .verifyComplete()
    }
    @Test
    fun `should not sign up when it auth exist in database`() {
        doReturn(Exception("Duplicate key").toMono<Exception>())
            .`when`(mockAuthRepository)
            .insert(auth)
        doReturn(Mono.empty<SenderResult<Void>>())
            .`when`(mockKafkaRepository)
            .createUser(user = userDetail.userDetail)
        StepVerifier.create(authController.signUp(userDetail, response))
            .expectNext("Auth already exist")
            .verifyComplete()
    }
    @Test
    fun `should login when auth exist in database`() {
        doReturn(Mono.just(true))
            .`when`(mockAuthRepository)
            .existsAuthByEmailAndHashedPassword(auth.email, auth.hashedPassword)
        StepVerifier.create(authController.login(auth, response))
            .expectNext("Login Successfully")
            .verifyComplete()
    }
    @Test
    fun `should not login when auth does not exist in database`() {
        doReturn(Mono.just(false))
            .`when`(mockAuthRepository)
            .existsAuthByEmailAndHashedPassword(auth.email, auth.hashedPassword)
        StepVerifier.create(authController.login(auth, response))
            .expectNext("Auth Not Found")
            .verifyComplete()
    }
    @Test
    fun `should update password when auth exist in database`() {
        val updatedAuth = Auth(email = auth.email, hashedPassword = "shh.. secret hashed password")
        doReturn(Mono.just(true))
            .`when`(mockAuthRepository)
            .existsById(auth.email)
        doReturn(Mono.just(updatedAuth))
            .`when`(mockAuthRepository)
            .save(updatedAuth)
        StepVerifier.create(authController.updatePassword(updatedAuth, response))
            .expectNext("Password Updated Successfully")
            .verifyComplete()
    }
    @Test
    fun `should not update password when auth does not exist in database`() {
        val updatedAuth = Auth(email = auth.email, hashedPassword = "shh.. secret hashed password")
        doReturn(Mono.just(false))
            .`when`(mockAuthRepository)
            .existsById(auth.email)
        StepVerifier.create(authController.updatePassword(updatedAuth, response))
            .expectNext("Auth Not Found")
            .verifyComplete()
    }
    @Test
    fun `should delete auth when auth exist in database`() {
        doReturn(Mono.just(true))
            .`when`(mockAuthRepository)
            .existsById(auth.email)
        doReturn(Mono.empty<Void>())
            .`when`(mockAuthRepository)
            .deleteById(auth.email)
        doReturn(Mono.empty<SenderResult<Void>>())
            .`when`(mockKafkaRepository)
            .deleteUser(userId = auth.email)
        StepVerifier.create(authController.deleteAuth(auth.email, response))
            .expectNext("Auth Deleted Successfully")
            .verifyComplete()
    }
    @Test
    fun `should not delete auth when auth does not exist in database`() {
        doReturn(Mono.just(false))
            .`when`(mockAuthRepository)
            .existsById(auth.email)
        doReturn(Mono.empty<SenderResult<Void>>())
            .`when`(mockKafkaRepository)
            .deleteUser(userId = auth.email)
        StepVerifier.create(authController.deleteAuth(auth.email, response))
            .expectNext("Auth Not Found")
            .verifyComplete()
    }
}