package _4.fortuna_reports.reports.usecases

import _4.fortuna_reports.entitymanager.UspNumber
import _4.fortuna_reports.reports.Report
import _4.fortuna_reports.reports.ReportRepository
import _4.fortuna_reports.utils.UseCase
import org.springframework.data.domain.PageRequest

class GetReports(
    private val repository: ReportRepository
): UseCase<GetReportsRequest, List<Report>>{
    override fun execute(request: GetReportsRequest): List<Report> {
        val pageable = PageRequest.of(request.page, request.size)
        return repository.findByStudentUspNumber(request.uspNumber, pageable)
    }
}

data class GetReportsRequest(
    val uspNumber: UspNumber,
    val page: Int = 0,
    val size: Int = 10
)