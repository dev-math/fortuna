package _4.fortuna_reports.reports

import jakarta.persistence.*
import java.time.LocalDateTime
import javax.annotation.processing.Generated

@Entity
@Table(name = "reports")
data class Report(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: String? = null,
    val studentUspNumber: String,
    val createdAt: LocalDateTime? = null,
    val articlesInWriting: Int,
    val articlesSubmitted: Int,
    val articlesAccepted: Int,
    val academicActivities: String,
    val researchSummary: String,
    val additionalDeclaration: String,
    val difficultyDetails: String? = null,
)