package _4.fortuna_bff.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "student_details")
data class StudentDetails(
    @Id val id: String = UUID.randomUUID().toString(),
    val course: String,
    val advisor: String,
    val lattesProfile: String,
    @OneToOne
    @JoinColumn(name = "user_id") val user: User,
)