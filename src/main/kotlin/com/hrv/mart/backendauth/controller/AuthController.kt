package com.hrv.mart.backendauth.controller

import com.hrv.mart.authlibrary.model.AuthRequest
import com.hrv.mart.backendauth.service.AuthService
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController (
    private val authService: AuthService
)
{
    @PostMapping
    fun getInfoFromJWT(
        @RequestBody authRequest: AuthRequest,
        response: ServerHttpResponse
    ) =
        authService
            .clientRequest(
                jwt = authRequest.jwt,
                userType = authRequest.userType,
                response = response
            )
}
