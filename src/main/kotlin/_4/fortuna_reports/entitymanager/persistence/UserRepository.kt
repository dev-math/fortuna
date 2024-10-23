package _4.fortuna_reports.entitymanager.persistence

import _4.fortuna_reports.entitymanager.User
import _4.fortuna_reports.entitymanager.UspNumber
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
    fun existsByUspNumber(uspNumber: UspNumber): Boolean
    fun findByEmail(email: String): User
}