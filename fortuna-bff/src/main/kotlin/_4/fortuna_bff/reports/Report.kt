package _4.fortuna_bff.reports

import _4.fortuna_bff.core.Student
import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import net.minidev.json.annotate.JsonIgnore
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "reports",
    indexes = [
        Index(name = "idx_reports_student_email", columnList = "student_email"),
        Index(name = "idx_reports_created_at", columnList = "createdAt")
    ]
)
data class Report(
    @Id val id: String = UUID.randomUUID().toString(),
    @ManyToOne
    @JoinColumn(name = "student_id")
    val student: Student,
    val articlesInWriting: Int,
    val articlesSubmitted: Int,
    val articlesAccepted: Int,
    val academicActivities: String,
    val researchSummary: String,
    val additionalDeclaration: String,
    val difficultyDetails: String? = null,
    val createdAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "report", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val feedbacks: List<Feedback> = emptyList()
)