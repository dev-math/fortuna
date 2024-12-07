package _4.fortuna_bff.core

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "students")
class Student(
    @Id val id: String = UUID.randomUUID().toString(),
    val course: String,
    val advisor: String,
    val lattesProfile: String,
    @OneToOne @JoinColumn(name = "user_id") val user: User,
)
