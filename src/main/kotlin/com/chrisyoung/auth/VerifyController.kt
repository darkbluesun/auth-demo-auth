package com.chrisyoung.auth

import io.fusionauth.jwt.domain.JWT
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@CrossOrigin(origins = ["http://localhost:3000"])
@RestController
class VerifyController
    (val jwtService: JwtService){
    @GetMapping("/verify")
    fun verifiy(
            @RequestHeader(name = "Authorization") auth: String
    ): JWT? {
        val token = auth.replace("Bearer ", "")
        return jwtService.verify(token)
    }
}