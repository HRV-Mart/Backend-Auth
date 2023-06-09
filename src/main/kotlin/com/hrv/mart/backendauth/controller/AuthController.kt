package com.hrv.mart.backendauth.controller

import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.model.UserType
import com.hrv.mart.backendauth.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault
import kotlin.jvm.optionals.getOrElse

@RestController
@RequestMapping("/auth")
class AuthController (
    private val authService: AuthService
)
{
    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping
    fun getInfoFromJWT(
        @RequestParam jwt: Optional<String>,
        @RequestParam userType: Optional<UserType>,
        response: ServerHttpResponse
    ): Mono<Auth > {
        return if (jwt.isEmpty) {
            response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
            Mono.empty()
        } else {
            authService.clientRequest(
                jwt = jwt.get(),
                userType = userType.getOrDefault(UserType.USER),
                response
            )
        }
    }
}
