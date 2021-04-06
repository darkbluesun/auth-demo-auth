package com.chrisyoung.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfiguration(
    val userDetailsService: UserDetailsService,
    val passwordEncoder: BCryptPasswordEncoder
): WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity?) {
        http?.csrf()?.disable()
            ?.authorizeRequests()
            ?.antMatchers("/token")?.permitAll()
            ?.antMatchers("/authorize")?.authenticated()
            ?.and()
            ?.formLogin()
            ?.and()
            ?.addFilterBefore(JwtAuthorizationFilter(JwtService()), BasicAuthenticationFilter::class.java)
    }

    public override fun userDetailsService(): org.springframework.security.core.userdetails.UserDetailsService {
        return UserDetailsService(null);
    }
}