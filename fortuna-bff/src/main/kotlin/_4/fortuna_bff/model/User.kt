package _4.fortuna_bff.model

import jakarta.persistence.*
import java.util.EnumSet
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id val id: String = UUID.randomUUID().toString(),

    @Column(unique = true) var uspNumber: String? = null,

    @Column(unique = true) val email: String,

    val name: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<UserRole> = mutableSetOf(),

) {
    enum class UserRole {
        UNKNOWN,
        STUDENT,
        PROFESSOR,
        CCP,
    }
}
