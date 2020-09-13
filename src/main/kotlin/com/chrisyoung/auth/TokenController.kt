package com.chrisyoung.auth

import javassist.NotFoundException
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class TokenRequest(
        val code: String
)

data class TokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val user: User
)

@RestController
class TokenController(val codeRepository: CodeRepository) {
    @PostMapping("/token")
    fun createToken(@RequestBody(required = true) tokenRequest: TokenRequest): TokenResponse {
        val code = codeRepository.findByCode(tokenRequest.code) ?: throw NotFoundException("Code not found")
        val token = JwtService().createAccessToken(code.client, code.user)
        return TokenResponse(token, "refresh-token", code.user)
    }
}