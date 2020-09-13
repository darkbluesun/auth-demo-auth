package com.chrisyoung.auth

import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import java.time.ZonedDateTime

class JwtService {
    fun createAccessToken(client: Client, user: User): String {
        val secret = "secret"
        val signer = HMACSigner.newSHA256Signer(secret)
        val jwt = JWT()
        jwt.addClaim("clientId", client.id)
        jwt.addClaim("userId", user.id)
        jwt.setIssuedAt(ZonedDateTime.now())
        jwt.setIssuer("auth-demo")
        return JWT.getEncoder().encode(jwt, signer)
    }
}