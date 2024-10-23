package _4.fortuna_reports.reports.usecases

import _4.fortuna_reports.entitymanager.persistence.UserRepository
import _4.fortuna_reports.reports.Report
import _4.fortuna_reports.reports.exceptions.InvalidSubmissionPeriodException
import _4.fortuna_reports.utils.UseCase
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import java.time.LocalDate

class CreateReport(private val userRepository: UserRepository): UseCase<CreateReport, Report> {
	override fun execute(request: CreateReportRequest): Report {
		if (!isWithinSubmissionPeriod()) throw InvalidSubmissionPeriodException("TODO: pensar nisso dps")

		val user = SecurityContextHolder.getContext().authentication.principal as OidcUser


		val report = Report(
			createdAt = LocalDate.now(),
			studentUspNumber = userRepository.findByEmail(user.email).uspNumber,
			academicActivities = request.academicActivities,
			articlesAccepted = request.articlesAccepted,
			articlesInWriting = request.articlesInWriting,
			articlesSubmitted = request.articlesSubmitted,
			difficultyDetails = request.difficultyDetails,
			researchSummary = request.researchSummary,
			additionalDeclaration = request.additionalDeclaration,
		)
	}

	private fun isWithinSubmissionPeriod(): Boolean {
		val currentDate = LocalDate.now()
		// se for entre outubro e dezembro pd mandar
		return currentDate.month.value in 10..12
	}
}

data class CreateReportRequest(
	val articlesInWriting: Int,
	val articlesSubmitted: Int,
	val articlesAccepted: Int,
	val academicActivities: String,
	val researchSummary: String,
	val additionalDeclaration: String,
	val difficultyDetails: String? = null,
)