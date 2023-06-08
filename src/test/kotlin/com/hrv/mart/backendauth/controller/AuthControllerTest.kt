package com.hrv.mart.backendauth.controller


import com.hrv.mart.backendauth.repository.AuthRepository
import com.hrv.mart.backendauth.repository.KafkaRepository
import com.hrv.mart.backendauth.service.AuthService
import org.mockito.Mockito.mock
class AuthControllerTest {
    private val mockAuthRepository = mock(AuthRepository::class.java)
    private val mockKafkaRepository = mock(KafkaRepository::class.java)
    private val authService = AuthService(mockAuthRepository, mockKafkaRepository)
    private val authController = AuthController(authService)

}