package _4.fortuna_bff.rest

import _4.fortuna_bff.reports.Feedback
import _4.fortuna_bff.reports.Report
import _4.fortuna_bff.reports.persistence.ReportRepository
import _4.fortuna_bff.reports.usecases.SendFeedback
import _4.fortuna_bff.security.CustomOidcUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController("/usp")
class FeedbackController(
    private val sendFeedback: SendFeedback,
    private val reportRepository: ReportRepository
) {
    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/professor/reports")
    fun getReports(@AuthenticationPrincipal principal: CustomOidcUser, pageable: Pageable): Page<Report> {
        return reportRepository.findByAdvisor(principal.email, pageable)
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'CCP')")
    @PostMapping("/feedback")
    fun sendFeedback(@RequestBody request: SendFeedback.Request): Feedback {
        return sendFeedback.execute(request)
    }

    @PreAuthorize("hasRole('CCP')")
    @GetMapping("/ccp/reports")
    fun getCcpReports(pageable: Pageable): Page<Report> {
        return reportRepository.findReportsWithSingleSuitableFeedback(pageable)
    }

    @PreAuthorize("hasAnyRole('PROFESSOR', 'CCP')")
    @GetMapping("/report/{id}")
    fun getReport(@PathVariable id: String): Report {
        return reportRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Relatório não encontrado com o ID: $id") }
    }
}