package _4.fortuna_bff.core.persistence

import _4.fortuna_bff.core.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): User?
    fun existsByUspNumber(uspNumber: String): Boolean
}
