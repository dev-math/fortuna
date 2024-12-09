package _4.fortuna_bff.reports.usecases

import _4.fortuna_bff.core.UseCase
import _4.fortuna_bff.core.persistence.StudentRepository
import _4.fortuna_bff.reports.Report
import _4.fortuna_bff.reports.SubmissionPeriod
import _4.fortuna_bff.reports.persistence.ReportRepository
import java.time.LocalDateTime

class SendReport(
    private val reportRepository: ReportRepository,
    private val studentRepository: StudentRepository
) : UseCase<SendReport.Request, Report> {

    override fun execute(request: Request): Report {
        val currentPeriod = SubmissionPeriod.getCurrentPeriod()
        val reportsForPeriod = reportRepository.findByStudentAndCreatedAtBetween(
            request.studentEmail,
            currentPeriod.start,
            currentPeriod.end
        )

        if (reportsForPeriod.size > 1) {
            throw MaxReportsPerPeriodException("Não é permitido mais de dois envios (original + revisão) por período.")
        }

        return reportRepository.save(
            Report(
                student = studentRepository.findByEmail(request.studentEmail)!!,
                articlesInWriting = request.articlesInWriting,
                articlesSubmitted = request.articlesSubmitted,
                articlesAccepted = request.articlesAccepted,
                academicActivities = request.academicActivities,
                researchSummary = request.researchSummary,
                additionalDeclaration = request.additionalDeclaration,
                difficultyDetails = request.difficultyDetails,
                createdAt = LocalDateTime.now()
            )
        )
    }

    data class Request(
        val studentEmail: String,
        val articlesInWriting: Int,
        val articlesSubmitted: Int,
        val articlesAccepted: Int,
        val academicActivities: String,
        val researchSummary: String,
        val additionalDeclaration: String,
        val difficultyDetails: String? = null,
    )
}

class MaxReportsPerPeriodException(e: String) : RuntimeException(e)