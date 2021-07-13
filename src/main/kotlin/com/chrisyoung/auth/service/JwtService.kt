package com.chrisyoung.auth.service

import com.chrisyoung.auth.Client
import com.chrisyoung.auth.User
import io.fusionauth.jwt.domain.JWT
import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class JwtService {
    private val secret = "secret"
    fun createAccessToken(client: Client, user: User): String {
        val signer = HMACSigner.newSHA256Signer(secret)
        val jwt = JWT()
        jwt.subject = user.email
        jwt.addClaim("client", client)
        jwt.addClaim("clientId", client.id)
        jwt.addClaim("user", user)
        jwt.addClaim("userId", user.id)
        jwt.addClaim("auth", arrayOf("ROLE_ADMIN"))
        jwt.setIssuedAt(ZonedDateTime.now())
        jwt.setIssuer("auth-demo")
        return JWT.getEncoder().encode(jwt, signer)
    }
    fun verify(token: String): JWT? {
        return JWT.getDecoder().decode(token, HMACVerifier.newVerifier(secret));
    }
}