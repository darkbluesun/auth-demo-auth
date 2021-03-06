package com.chrisyoung.auth

import javassist.NotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class TokenRequest(
        val code: String
)

data class TokenResponse(
        val access_token: String,
        val refresh_token: String,
        val user: User
)

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class TokenController(
    val codeRepository: CodeRepository,
    val jwtService: JwtService
    ) {
    @PostMapping("/token")
    fun createToken(@RequestBody(required = true) tokenRequest: TokenRequest): ResponseEntity<Any> {
        val code = codeRepository.findByCode(tokenRequest.code) ?: return  ResponseEntity.notFound().build()
        val token = jwtService.createAccessToken(code.client, code.user)
        return ResponseEntity.ok().body(TokenResponse(token, "refresh-token", code.user))
    }
}