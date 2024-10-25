package _4.fortuna_reports.reports.usecases

import _4.fortuna_reports.entitymanager.User
import _4.fortuna_reports.entitymanager.UspNumber
import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "feedbacks")
data class Feedback(
    @Id val id: String = UUID.randomUUID().toString(),
    val sendedBy: String,
    val reportId: String,
    val feedbackDetails: String,
    val reportSituation: ReportSituation,
)

enum class ReportSituation {
    SUITABLE,
    NOT_SUITABLE,
    SUITABLE_WITH_RESERVATIONS,
}

// report
// - student: student
// - orientador: User
// - dados do relatorio
// - feedbacks: List<Feedback>

// Feedback
// - sendedBy: User
// - StudentName: Student
// - feedbackTarget: ReportId
// - feedbackDetails: String
// - ReportSituation: (adequado, ñ adequado, adequado com ressalvas)
// Feedback
// - sendedBy: User
// - StudentName: Student
// - feedbackTarget: ReportId
// - feedbackDetails: String
// - ReportSituation: (adequado, ñ adequado, adequado com ressalvas)
