package com.chrisyoung.auth

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringRunner::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTests() {

    @Autowired
    private val context: WebApplicationContext? = null

    private lateinit var mvc: MockMvc

    @BeforeAll
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @Test
    @WithMockUser(username = "test@test.com")
    fun `Assert that the form is displayed`() {
        mvc.get("/authorize?state=1234&clientId=2") {}.andExpect {
            status { isOk }
        }
    }
    @Test
    @WithMockUser(username = "test@test.com")
    fun `Assert that wrong clientId fails`() {
        mvc.get("/authorize?state=1234&clientId=5") {}.andExpect {
            status { isNotFound }
        }
    }
    @Test
    @WithMockUser(username = "test@test.com")
    fun `Assert that wrong clientId fails POST`() {
        mvc.post("/authorize?state=1234&clientId=5") {}.andExpect {
            status { isNotFound }
        }
    }
    @Test
    @WithMockUser(username = "notfound@test.com")
    fun `Assert that wrong user fails POST`() {
        mvc.post("/authorize?state=1234&clientId=2") {}.andExpect {
            status { isNotFound }
        }
    }
    @WithMockUser(username = "test@test.com")
    @Test
    fun `Assert that the redirect happens`() {
        mvc.post("/authorize?state=1234&clientId=2") {}.andExpect {
            status { isFound }
        }
    }
}