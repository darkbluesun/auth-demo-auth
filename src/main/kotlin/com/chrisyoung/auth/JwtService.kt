package com.chrisyoung.auth

import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import java.time.ZonedDateTime

class JwtService {
    private val secret = "secret"
    fun createAccessToken(client: Client, user: User): String {
        val signer = HMACSigner.newSHA256Signer(secret)
        val jwt = JWT()
        jwt.addClaim("client", client)
        jwt.addClaim("clientId", client.id)
        jwt.addClaim("user", user)
        jwt.addClaim("userId", user.id)
        jwt.setIssuedAt(ZonedDateTime.now())
        jwt.setIssuer("auth-demo")
        return JWT.getEncoder().encode(jwt, signer)
    }
    fun verify(token: String): JWT? {
        return JWT.getDecoder().decode(token, HMACVerifier.newVerifier(secret));
    }
}