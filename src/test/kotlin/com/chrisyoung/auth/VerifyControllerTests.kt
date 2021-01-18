package com.chrisyoung.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VerifyControllerTests(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `not authorized`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.setBearerAuth("test123")
        val request = HttpEntity({}, headers)
        val entity = restTemplate.getForEntity<String>("/verify", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}