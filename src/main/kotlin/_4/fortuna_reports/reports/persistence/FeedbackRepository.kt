package _4.fortuna_reports.reports.persistence

import _4.fortuna_reports.reports.usecases.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository: JpaRepository<Feedback, String>
