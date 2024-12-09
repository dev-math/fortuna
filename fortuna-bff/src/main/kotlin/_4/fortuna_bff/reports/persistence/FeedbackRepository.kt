package _4.fortuna_bff.reports.persistence

import _4.fortuna_bff.reports.Feedback
import org.springframework.data.jpa.repository.JpaRepository

interface FeedbackRepository: JpaRepository<Feedback, String>