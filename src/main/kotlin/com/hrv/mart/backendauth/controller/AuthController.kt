package com.hrv.mart.backendauth.controller

import com.hrv.mart.backendauth.model.UserType
import com.hrv.mart.backendauth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Optional

@RestController
@RequestMapping("/auth")
class AuthController (
    private val authService: AuthService
)
{
    @GetMapping
    suspend fun getInfoFromJWT(
        @RequestParam jwt: Optional<String>,
        @RequestParam userType: Optional<UserType>,
        response: ServerHttpResponse
    ): String {
        if (jwt.isEmpty) {
            response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            return "Please send JWT"
        }
        else if (userType.isEmpty) {
            return authService.clientRequest(
                jwt = jwt.get(),
                UserType.USER,
                response
            )
        }
        else {
            return authService.clientRequest(
                jwt = jwt.get(),
                userType.get(),
                response
            )
        }
    }
}
