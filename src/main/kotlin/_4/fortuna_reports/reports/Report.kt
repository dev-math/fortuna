package _4.fortuna_reports.reports

import _4.fortuna_reports.entitymanager.UspNumber
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "reports")
data class Report(
    @Id val id: String = UUID.randomUUID().toString(),
    val studentUspNumber: UspNumber,
    val articlesInWriting: Int,
    val articlesSubmitted: Int,
    val articlesAccepted: Int,
    val academicActivities: String,
    val researchSummary: String,
    val additionalDeclaration: String,
    val difficultyDetails: String? = null,
    val createdAt: LocalDateTime? = null,
)