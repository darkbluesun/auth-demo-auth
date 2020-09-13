package com.chrisyoung.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.getForEntity
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTests(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `Assert that the form is displayed`() {
        val entity = restTemplate.getForEntity<String>("/authorize?state=1234&clientId=2")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
    }
    @Test
    fun `Assert that the redirect happens if not logged in`() {
        val entity = restTemplate.postForEntity<String>("/authorize?state=1234&clientId=2")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.FOUND)
    }
    @Test
    fun `Assert that wrong clientId fails`() {
        val entity = restTemplate.getForEntity<String>("/authorize?state=1234&clientId=1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
    @Test
    fun `Assert that wrong clientId fails POST`() {
        val entity = restTemplate.postForEntity<String>("/authorize?state=1234&clientId=1")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
    @Test
    fun `Assert that the redirect happens`() {
        val loginResponse = restTemplate.postForEntity<String>("/login?username=testuser&password=testpassword")
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        val sessionCookie = loginResponse.headers.get("Set-Cookie")?.get(0)?.split(';')?.get(0);
        val headers = HttpHeaders();
        headers.add("Cookie", sessionCookie)
        val entity = HttpEntity<Any>(headers)
        val result = restTemplate.exchange<String>("/authorize?state=5678&clientId=2", HttpMethod.POST, entity)
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}