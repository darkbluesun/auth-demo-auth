package com.chrisyoung.auth

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RepositoriesTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository
) {
    @Test
    fun `When findByLogin then return user`() {
        val oldMate = User("oldmate", "password1")
        entityManager.persist(oldMate)
        entityManager.flush()
        val user = userRepository.findByUsername("oldmate")
    }
}