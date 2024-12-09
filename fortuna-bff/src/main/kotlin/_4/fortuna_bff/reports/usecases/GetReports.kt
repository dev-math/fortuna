package _4.fortuna_bff.reports.usecases

import java.time.LocalDateTime

data class ReportDTO(
    val id: String,
    val articlesInWriting: Int,
    val articlesSubmitted: Int,
    val articlesAccepted: Int,
    val academicActivities: String,
    val createdAt: LocalDateTime
)