package _4.fortuna_bff.services

import _4.fortuna_bff.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, String> {
    fun findByEmail(email: String): User?
    fun existsByUspNumber(uspNumber: String): Boolean
}
