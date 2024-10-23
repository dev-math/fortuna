package _4.fortuna_reports.entitymanager.persistence

import _4.fortuna_reports.entitymanager.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByUspNumber(uspNumber: String): Boolean
    fun findByEmail(email: String): User
}