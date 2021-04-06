package com.chrisyoung.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VerifyControllerTests(@Autowired val restTemplate: TestRestTemplate, @Autowired val jwtService: JwtService) {
    @Test
    fun `not authorized`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBearerAuth("test123")
        val request = HttpEntity({}, headers)
        val entity = restTemplate.getForEntity<String>("/verify", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    @Test
    fun `authorized`() {
        val token = jwtService.createAccessToken(
            Client("Test Client", "secret string", "redirect URL"),
            User("test@test.com", "0412345678", "Test", "User", "Password")
        )
        val headers = HttpHeaders()
        headers.setBearerAuth(token)
        val request = HttpEntity(null, headers)
        val entity = restTemplate.exchange<String>("/verify", HttpMethod.GET, request);
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK);
    }
}