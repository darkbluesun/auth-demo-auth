package com.chrisyoung.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class AuthApplication

fun main(args: Array<String>) {
	runApplication<AuthApplication>(*args)
}
