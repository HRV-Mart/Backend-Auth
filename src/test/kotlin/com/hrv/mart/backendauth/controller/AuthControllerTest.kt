package com.hrv.mart.backendauth.controller


import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.model.UserType
import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import com.hrv.mart.backendauth.service.AuthService
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono
import reactor.kafka.sender.SenderResult
import java.util.*

class AuthControllerTest {
    private val mockAuthRepository = mock(AuthRepository::class.java)
    private val mockKafkaRepository = mock(KafkaRepository::class.java)
    private val response = mock(ServerHttpResponse::class.java)

    private val authService = AuthService(mockAuthRepository, mockKafkaRepository)
    private val authController = AuthController(authService)

    @Test
    fun `should return login successful message if jwt is valid`(): Unit = runBlocking {
        val jwt = "A_VALID_JWT"
        val userType = UserType.USER

        val auth = Auth(
            email = "test@test.com",
            emailVerification = true,
            createdAt = Date().toString(),
            updatedAt = Date().toString(),
            name = "Test User"
        )
        doReturn(auth)
            .`when`(mockAuthRepository)
            .getAuthAccount(jwt)
        doReturn(Mono.empty<SenderResult<Void>>())
            .`when`(mockKafkaRepository)
            .createUser(auth.toUser())
        val expected = "Login Successful"
        val actual = authController
            .getInfoFromJWT(
                jwt = Optional.of(jwt),
                userType = Optional.of(userType),
                response = response
            )
        Assertions.assertEquals(expected, actual)
    }
}