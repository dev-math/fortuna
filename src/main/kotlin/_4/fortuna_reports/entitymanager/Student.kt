package _4.fortuna_reports.entitymanager

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "students")
data class Student(
    @Id val id: String = UUID.randomUUID().toString(),
    val course: String,
    val advisor: String,
    val lattesProfile: String,
    @OneToOne
    @JoinColumn(name = "user_id") val user: User,
)