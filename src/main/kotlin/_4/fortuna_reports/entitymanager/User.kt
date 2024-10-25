package _4.fortuna_reports.entitymanager

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
data class User(
    @Id val id: String = UUID.randomUUID().toString(),
    @Column(unique = true) val uspNumber: UspNumber,
    @Column(unique = true) val email: String,
    val name: String,
    val role: UserRole
)

@JvmInline
value class UspNumber(private val s: String)

enum class UserRole {
    STUDENT, PROFESSOR
}
