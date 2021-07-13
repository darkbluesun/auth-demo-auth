package com.chrisyoung.auth.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer


@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfig(
    private val passwordEncoder: BCryptPasswordEncoder
): AuthorizationServerConfigurerAdapter() {

    private val clientID = "auth-frontend"
    private val clientSecret = "secret"
    private val redirectURLs = "http://localhost:3000/login"

    @Throws(Exception::class)
    override fun configure(
        oauthServer: AuthorizationServerSecurityConfigurer
    ) {
        oauthServer.tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
            .withClient(clientID)
            .secret(passwordEncoder.encode(clientSecret))
            .authorizedGrantTypes("authorization_code")
            .scopes("user_info")
            .autoApprove(true)
            .redirectUris(redirectURLs)
    }
}