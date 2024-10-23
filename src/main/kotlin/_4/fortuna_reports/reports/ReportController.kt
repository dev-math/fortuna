package _4.fortuna_reports.reports

import _4.fortuna_reports.reports.usecases.GetReports
import _4.fortuna_reports.reports.usecases.SubmitReport
import _4.fortuna_reports.reports.usecases.SubmitReportRequest
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/reports")
@RestController
class ReportController(
	private val submitReport: SubmitReport,
	private val getReports: GetReports
) {
	@PostMapping
    fun createReport(@RequestBody request: SubmitReportRequest): Report {
		// validate request
		return submitReport.execute(request)
	}
}
