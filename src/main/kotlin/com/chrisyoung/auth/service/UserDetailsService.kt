package com.chrisyoung.auth.service

import com.chrisyoung.auth.MyUserDetails
import com.chrisyoung.auth.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(@Autowired val userRepository: UserRepository?) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        username?.also {
            userRepository?.findByEmail(username)?.also {
                return MyUserDetails(it)
            }
        }
        throw UsernameNotFoundException("user not found")
    }
}