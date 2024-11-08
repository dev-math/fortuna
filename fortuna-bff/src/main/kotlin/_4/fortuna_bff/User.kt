package _4.fortuna_bff

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id val id: String = UUID.randomUUID().toString(),
    @Column(unique = true) val uspNumber: String? = null,
    @Column(unique = true) val email: String,
    val name: String,
    val roles: List<UserRole>
) {
    enum class UserRole {
        UNKNOWN,
        STUDENT,
        PROFESSOR,
        CCP,
    }
}
