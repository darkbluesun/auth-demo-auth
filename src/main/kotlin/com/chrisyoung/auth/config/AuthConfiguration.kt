package com.chrisyoung.auth.config

import com.chrisyoung.auth.*
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class AuthConfiguration {
    @Bean
    fun databaseInitializer(
        userRepository: UserRepository,
        clientRepository: ClientRepository,
        codeRepository: CodeRepository
    ) = ApplicationRunner {
        val user = userRepository.save(User("test@test.com", "0412345678", "Test", "User", passwordEncoder().encode("password")))
        val client = clientRepository.save(Client("Website", "secret", "http://localhost:3000/login"))
        codeRepository.save(Code("1234", client, user))
    }
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}