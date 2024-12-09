package _4.fortuna_bff.reports.persistence

import _4.fortuna_bff.reports.Report
import _4.fortuna_bff.reports.usecases.ReportDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ReportRepository: JpaRepository<Report, String> {
    @Query("""
        SELECT r
        FROM Report r
        WHERE r.student.user.email = :email
          AND r.createdAt BETWEEN :startDate AND :endDate
    """)
    fun findByStudentAndCreatedAtBetween(
        @Param("email") email: String,
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<Report>

    @Query("SELECT new _4.fortuna_bff.reports.usecases.ReportDTO(r.id, r.articlesInWriting, r.articlesSubmitted, r.articlesAccepted, r.academicActivities, r.createdAt) FROM Report r WHERE r.student.email = :email")
    fun findReportsByStudentEmail(email: String, pageable: Pageable): Page<ReportDTO>

    @Query(
        "SELECT r FROM Report r " +
                "JOIN r.student s " +
                "WHERE s.advisor = :advisorEmail"
    )
    fun findByAdvisor(advisorEmail: String, pageable: Pageable): Page<Report>

    @Query(
        """
    SELECT r 
    FROM Report r 
    JOIN r.feedbacks f 
    WHERE f.reportSituation = _4.fortuna_bff.reports.Feedback.Situation.SUITABLE
    GROUP BY r 
    HAVING COUNT(f) = 1
    """
    )
    fun findReportsWithSingleSuitableFeedback(pageable: Pageable): Page<Report>
}