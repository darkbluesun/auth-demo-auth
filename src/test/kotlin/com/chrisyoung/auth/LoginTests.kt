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
class LoginTests(@Autowired val restTemplate: TestRestTemplate) {
    @Test
    fun `Assert that login page contains a form`() {
        val entity = restTemplate.getForEntity<String>("/login")
        assertThat(entity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(entity.body).contains("<form");
    }

    @Test
    fun `Assert user login fail`() {
        val loginResponse = restTemplate.postForEntity<String>("/login?email=test@test.com&password=wrongpassword")
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(loginResponse.body).contains("user not found")
    }

    @Test
    fun `Assert user login`() {
        val loginResponse = restTemplate.postForEntity<String>("/login?email=test@test.com&password=password")
        assertThat(loginResponse.statusCode).isEqualTo(HttpStatus.OK)
        val sessionCookie = loginResponse.headers.get("Set-Cookie")?.get(0)?.split(';')?.get(0);
        val headers = HttpHeaders();
        headers.add("Cookie", sessionCookie)
        val entity = HttpEntity<Any>(headers)
        val result = restTemplate.exchange<String>("/login", HttpMethod.GET, entity)
        assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    }
}