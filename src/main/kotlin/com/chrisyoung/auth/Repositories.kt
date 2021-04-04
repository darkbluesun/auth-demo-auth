package com.chrisyoung.auth

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findByEmail(email: String): User?
}

interface ClientRepository : CrudRepository<Client, Long> {

}

interface CodeRepository: CrudRepository<Code, Long> {
    fun findByCode(code: String): Code?
}