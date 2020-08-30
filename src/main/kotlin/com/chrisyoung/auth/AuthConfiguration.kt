package com.chrisyoung.auth

import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuthConfiguration {
    @Bean
    fun databaseInitializer(userRepository: UserRepository) = ApplicationRunner {
        userRepository.save(User("testuser", "testpassword"))
    }
}