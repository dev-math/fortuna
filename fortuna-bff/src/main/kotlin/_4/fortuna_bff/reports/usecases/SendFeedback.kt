package _4.fortuna_bff.reports.usecases

import _4.fortuna_bff.core.UseCase
import _4.fortuna_bff.core.User
import _4.fortuna_bff.core.persistence.UserRepository
import _4.fortuna_bff.reports.Feedback
import _4.fortuna_bff.reports.persistence.FeedbackRepository
import _4.fortuna_bff.reports.persistence.ReportRepository
import java.time.LocalDateTime

class SendFeedback(
    private val feedbackRepository: FeedbackRepository,
    private val reportRepository: ReportRepository,
    private val userRepository: UserRepository
): UseCase<SendFeedback.Request, Feedback> {
    data class Request(
        val authorEmail: String,
        val reportId: String,
        val feedbackDetails: String,
        val reportSituation: Feedback.Situation,
    )

    override fun execute(request: Request): Feedback {
        val report = reportRepository.findById(request.reportId).orElseThrow {
            throw IllegalStateException("O relatório indicado não existe.")
        }

        val user = userRepository.findByEmail(request.authorEmail)!!

        val studentReports = reportRepository.findByStudentAndCreatedAtBetween(
            report.student.email,
            LocalDateTime.of(report.createdAt!!.year, report.createdAt!!.month, 1, 0, 0),
            LocalDateTime.of(report.createdAt!!.year, report.createdAt!!.month, 31, 23, 59)
        )

        if (studentReports.last().id != report.id) {
            throw IllegalStateException("Esse relatório possui uma resubmissão, envie seu feedback nele.")
        }

        when (report.feedbacks.size) {
            0 -> {
                val isAdvisorSending = report.student.advisor.equals(request.authorEmail) && user.roles.contains(User.Role.PROFESSOR)
                if (!isAdvisorSending) {
                    throw UserWithoutPermissionException()
                }
            }
            1 -> {
                if (!user.roles.contains(User.Role.CCP)) {
                    throw UserWithoutPermissionException()
                }
            }
            else -> {
                throw UserWithoutPermissionException()
            }
        }

        return feedbackRepository.save(Feedback(
            sentBy = request.authorEmail,
            feedbackDetails = request.feedbackDetails,
            reportSituation = request.reportSituation,
            report = report,
        ))
    }
}

class UserWithoutPermissionException() : RuntimeException()
