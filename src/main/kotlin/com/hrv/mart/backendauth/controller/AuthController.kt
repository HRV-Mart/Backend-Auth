package com.hrv.mart.backendauth.controller

import com.hrv.mart.backendauth.model.Auth
import com.hrv.mart.backendauth.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController (
    @Autowired
    private val authService: AuthService
)
{
    @PostMapping("/signup")
    fun signUp(@RequestBody auth: Auth, response: ServerHttpResponse) =
        authService.signUp(auth, response)
    @PostMapping("/login")
    fun login(@RequestBody auth: Auth, response: ServerHttpResponse) =
        authService.login(auth, response)
    @PutMapping
    fun updatePassword(@RequestBody auth: Auth, response: ServerHttpResponse) =
        authService.updatePassword(auth, response)
    @DeleteMapping("/{emailId}")
    fun deleteAuth(@PathVariable emailId: String, response: ServerHttpResponse) =
        authService.deleteAuth(emailId, response)
}
