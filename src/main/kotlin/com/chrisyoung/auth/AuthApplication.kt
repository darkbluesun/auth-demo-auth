package com.chrisyoung.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
@EnableAuthorizationServer
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
