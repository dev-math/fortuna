package _4.fortuna_bff.rest

import _4.fortuna_bff.reports.Report
import _4.fortuna_bff.reports.persistence.ReportRepository
import _4.fortuna_bff.reports.usecases.ReportDTO
import _4.fortuna_bff.reports.usecases.SendReport
import _4.fortuna_bff.security.CustomOidcUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/student")
@PreAuthorize("hasRole('STUDENT')")
class StudentController(
    private val reportRepository: ReportRepository,
    private val sendReport: SendReport
) {
    @PostMapping("/reports")
    fun sendReport(@RequestBody request: SendReport.Request): Report {
        return sendReport.execute(request)
    }

    @GetMapping("/reports")
    fun getReports(@AuthenticationPrincipal principal: CustomOidcUser, pageable: Pageable): Page<ReportDTO> {
        return reportRepository.findReportsByStudentEmail(principal.email, pageable)
    }

    @GetMapping("/reports/{id}")
    fun getStudentReport(@AuthenticationPrincipal principal: CustomOidcUser, @PathVariable id: String): Report {
        return reportRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Relatório não encontrado com o ID: $id") }
            .also {
                if (it.student.email != principal.email) {
                    throw IllegalAccessException("Você não tem permissão para acessar este relatório.")
                }
            }
    }
}