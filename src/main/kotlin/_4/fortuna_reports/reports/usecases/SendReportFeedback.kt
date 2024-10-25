package _4.fortuna_reports.reports.usecases

import _4.fortuna_reports.utils.UseCase
import _4.fortuna_reports.reports.persistence.FeedbackRepository
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.core.context.SecurityContextHolder

class SendReportFeedback(
    private val repository: FeedbackRepository,
): UseCase<SendFeedbackRequest, Feedback> {
    override fun execute(request: SendFeedbackRequest): Feedback {
        val user = SecurityContextHolder.getContext().authentication.principal as OidcUser

        return repository.save(
            Feedback(
                sendedBy = user.email, //TODO: arrumar isso
                reportId = request.reportId,
                feedbackDetails = request.feedbackDetails,
                reportSituation = request.reportSituation,
            )
        )
    }   
}

data class SendFeedbackRequest(
    val reportId: String,
    val feedbackDetails: String,
    val reportSituation: ReportSituation,
)
