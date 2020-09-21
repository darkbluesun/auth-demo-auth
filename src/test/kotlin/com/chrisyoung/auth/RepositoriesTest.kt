package com.chrisyoung.auth

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RepositoriesTest @Autowired constructor(
        val entityManager: TestEntityManager,
        val userRepository: UserRepository,
        val codeRepository: CodeRepository
) {
    @Test
    fun `When findByLogin then return user`() {
        val oldMate = User("oldmate", "password1")
        entityManager.persist(oldMate)
        entityManager.flush()
        val user = userRepository.findByUsername("oldmate")
        assertThat(user).isEqualTo(oldMate)
    }
    @Test
    fun `When findByCode then return code`() {
        val client = Client("client", "secret", "redirect")
        val user = User("user", "password")
        val code = Code("1234", client, user)
        entityManager.persist(code)
        entityManager.persist(client)
        entityManager.persist(user)
        entityManager.flush()
        val result = codeRepository.findByCode("1234")
        assertThat(result).isEqualTo(code)
    }
}