package _4.fortuna_reports.entitymanager

import jakarta.persistence.*

@Entity
@Table(name = "students")
data class Student(
    @Id @GeneratedValue val id: Long? = null,
    val course: String,
    val advisor: String,
    val lattesProfile: String,
    @OneToOne
    @JoinColumn(name = "user_id") val user: User,
)