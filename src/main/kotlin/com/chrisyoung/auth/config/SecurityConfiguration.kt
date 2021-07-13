package com.chrisyoung.auth.config

import com.chrisyoung.auth.JwtAuthorizationFilter
import com.chrisyoung.auth.service.JwtService
import com.chrisyoung.auth.service.UserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity()
@Order(-1)
class SecurityConfiguration(
    val userDetailsService: UserDetailsService,
    val passwordEncoder: BCryptPasswordEncoder
): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity?) {
        http?.cors()?.and()?.csrf()?.disable()
            ?.authorizeRequests()
            ?.antMatchers("/oauth/token")?.permitAll()
            ?.antMatchers("/authorize")?.authenticated()
            ?.and()
            ?.formLogin()
            ?.and()
            ?.addFilterBefore(JwtAuthorizationFilter(JwtService()), BasicAuthenticationFilter::class.java)
    }

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.addAllowedOrigin("http://localhost:3000")
        configuration.addAllowedMethod("POST")
        configuration.addAllowedHeader("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}