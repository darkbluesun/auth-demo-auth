package com.chrisyoung.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.exchange
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsersControllerTests(
    @Autowired val restTemplate: TestRestTemplate,
    @Autowired val jwtService: JwtService,
    @Autowired val userRepository: UserRepository
    ){
    @Test
    fun `list users`() {
        val request = HttpEntity(null, getHeaders())
        val response = restTemplate.exchange<Array<User>>("/users", HttpMethod.GET, request)
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.firstOrNull()?.email).isEqualTo("test@test.com")
    }
    @Test
    fun `get user`() {
        val userId = userRepository.findAll().firstOrNull()?.id
        assertThat(userId).isNotNull();
        val response = restTemplate.exchange<User>("/users/$userId", HttpMethod.GET, HttpEntity(null, getHeaders()))
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?.email).isEqualTo("test@test.com")
    }
    @Test
    fun `post user`() {
        val userCreateBody = UserCreateBody(
            "test2@test.com",
            "0412345679",
            "second",
            "test"
        )
        val request = HttpEntity(userCreateBody, getHeaders())
        val response = restTemplate.postForEntity<User>("/users", request)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.email).isEqualTo(userCreateBody.email)
    }
    fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        val jwt = jwtService.createAccessToken(
            Client("test client", "secret", "redirect url"),
            User("test@test.com", "0412345678", "test", "user", "password")
        )
        headers.setBearerAuth(jwt);
        return headers
    }
}