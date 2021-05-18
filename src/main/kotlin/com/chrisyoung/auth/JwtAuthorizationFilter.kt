package com.chrisyoung.auth

import com.chrisyoung.auth.service.JwtService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(private val jwtService: JwtService) : OncePerRequestFilter() {

    companion object {
        private fun String.extractToken() = split(" ").last()
    }

    private fun getToken(request: HttpServletRequest): String?
    {
        return request.getHeader("Authorization")?.also {
            return if (it.contains("bearer")) {
                it.extractToken()
            } else {
                null;
            }
        }
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        getToken(request)?.also { token ->
            jwtService.verify(token)?.also {
                val authorities = ArrayList<GrantedAuthority>()
                (it.allClaims["auth"] as List<*>).forEach { role -> authorities.add(SimpleGrantedAuthority(role.toString())) }
                SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                    it.subject,
                    null,
                    authorities
                )
            }
        }

        filterChain.doFilter(request, response)
    }
}