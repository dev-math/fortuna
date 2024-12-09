package _4.fortuna_bff.reports.usecases

import _4.fortuna_bff.core.Student
import _4.fortuna_bff.reports.Report
import _4.fortuna_bff.reports.SubmissionPeriod
import _4.fortuna_bff.reports.persistence.ReportRepository
import _4.fortuna_bff.core.persistence.StudentRepository
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class SendReportTest {
    private lateinit var reportRepository: ReportRepository
    private lateinit var studentRepository: StudentRepository
    private lateinit var sendReport: SendReport
    private val currentPeriod = SubmissionPeriod.SEMESTER_1

    @BeforeEach
    fun setUp() {
        reportRepository = mockk()
        studentRepository = mockk()
        sendReport = SendReport(reportRepository, studentRepository)

        mockkObject(SubmissionPeriod)
        every { SubmissionPeriod.getCurrentPeriod() } returns currentPeriod
        every { SubmissionPeriod.isWithinSubmissionPeriod() } returns true
    }

    private fun createReport(
        student: Student,
        articlesInWriting: Int = 0,
        articlesSubmitted: Int = 0,
        articlesAccepted: Int = 0,
        academicActivities: String = "Test Activities",
        researchSummary: String = "Test Summary",
        additionalDeclaration: String = "Test Declaration",
        difficultyDetails: String? = null,
        createdAt: LocalDateTime = LocalDateTime.now()
    ) = Report(
        student = student,
        articlesInWriting = articlesInWriting,
        articlesSubmitted = articlesSubmitted,
        articlesAccepted = articlesAccepted,
        academicActivities = academicActivities,
        researchSummary = researchSummary,
        additionalDeclaration = additionalDeclaration,
        difficultyDetails = difficultyDetails,
        createdAt = createdAt
    )

    @Test
    fun `should throw MaxReportsPerPeriodException if more than one report is submitted in the current period`(): Unit {
        val studentEmail = "student@usp.br"

        val student = mockk<Student>()
        val reports = listOf(
            createReport(student, createdAt = LocalDateTime.now()),
            createReport(student, createdAt = LocalDateTime.now())
        )

        every { reportRepository.findByStudentAndCreatedAtBetween(studentEmail, currentPeriod.start, currentPeriod.end) } returns reports

        assertFailsWith<MaxReportsPerPeriodException> {
            sendReport.execute(SendReport.Request(
                studentEmail = studentEmail,
                articlesInWriting = 0,
                articlesSubmitted = 0,
                articlesAccepted = 0,
                academicActivities = "Test",
                researchSummary = "Test Summary",
                additionalDeclaration = "Test Declaration"
            ))
        }
    }

    @Test
    fun `should create a new report when submission is within allowed limit`() {
        val studentEmail = "student@usp.br"
        val currentPeriod = SubmissionPeriod.SEMESTER_1

        val student = mockk<Student>()
        val reportRequest = SendReport.Request(
            studentEmail = studentEmail,
            articlesInWriting = 2,
            articlesSubmitted = 1,
            articlesAccepted = 1,
            academicActivities = "Test Activities",
            researchSummary = "Test Summary",
            additionalDeclaration = "Test Declaration"
        )

        every { studentRepository.findByEmail(studentEmail) } returns student
        every { reportRepository.findByStudentAndCreatedAtBetween(studentEmail, currentPeriod.start, currentPeriod.end) } returns emptyList()

        val createdReport = createReport(
            student = student,
            articlesInWriting = reportRequest.articlesInWriting,
            articlesSubmitted = reportRequest.articlesSubmitted,
            articlesAccepted = reportRequest.articlesAccepted,
            academicActivities = reportRequest.academicActivities,
            researchSummary = reportRequest.researchSummary,
            additionalDeclaration = reportRequest.additionalDeclaration,
            createdAt = LocalDateTime.now()
        )
        every { reportRepository.save(any()) } returns createdReport

        val result = sendReport.execute(reportRequest)

        assertEquals(createdReport, result)
        verify { reportRepository.save(any()) }
        verify { studentRepository.findByEmail(studentEmail) }
    }
}
