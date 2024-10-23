package _4.fortuna_reports.entitymanager

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue val id: Long? = null,
    @Column(unique = true) val uspNumber: String,
    @Column(unique = true) val email: String,
    val name: String,
    val role: UserRole
)

enum class UserRole {
    STUDENT, PROFESSOR
}