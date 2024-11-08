package _4.fortuna_bff

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): User?
}