package _4.fortuna_reports.reports

import _4.fortuna_reports.entitymanager.UspNumber
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository: JpaRepository<Report, String> {
    fun findByStudentUspNumber(uspNumber: UspNumber, page: Pageable): List<Report>
}