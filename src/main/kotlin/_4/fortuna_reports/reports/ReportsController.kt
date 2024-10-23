package _4.fortuna_reports.reports

import _4.fortuna_reports.reports.usecases.CreateReportRequest
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/reports")
@RestController
class ReportsController {
	@PostMapping
    fun createReport(@RequestBody request: CreateReportRequest): Report {
		// validate request
		// salvar relatorio
		// notificar professor
	}
}
