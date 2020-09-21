package com.chrisyoung.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TokenControllerTests(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `Get a token successfully`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(TokenRequest("1234"), headers)
        val entity = restTemplate.postForEntity<String>("/token", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `Bad code`() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(TokenRequest("wrong"), headers)
        val entity = restTemplate.postForEntity<String>("/token", request)
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}