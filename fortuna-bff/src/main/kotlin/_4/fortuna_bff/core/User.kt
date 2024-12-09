package _4.fortuna_bff.core

import jakarta.persistence.*
import java.util.EnumSet
import java.util.UUID

@Entity
@Table(
    name = "users",
    indexes = [Index(name = "idx_users_email", columnList = "email", unique = true)]
)
class User(
    @Id val id: String = UUID.randomUUID().toString(),

    @Column(unique = true) var uspNumber: String? = null,

    @Column(unique = true) val email: String,

    val name: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "roles", joinColumns = [JoinColumn(name = "user_id")])
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<Role> = mutableSetOf(),

) {
    enum class Role {
        UNKNOWN,
        STUDENT,
        PROFESSOR,
        CCP,
    }
}
