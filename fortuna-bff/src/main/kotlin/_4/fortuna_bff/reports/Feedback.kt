package _4.fortuna_bff.reports

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "feedbacks")
data class Feedback(
    @Id val id: String = UUID.randomUUID().toString(),
    val sentBy: String,
    val feedbackDetails: String,
    val reportSituation: Situation,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    val report: Report
) {
    enum class Situation {
        SUITABLE,
        NOT_SUITABLE,
        SUITABLE_WITH_RESERVATIONS,
    }
}
