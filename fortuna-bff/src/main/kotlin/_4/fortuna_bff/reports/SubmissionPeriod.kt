package _4.fortuna_bff.reports

import java.time.LocalDateTime
import java.time.Month

enum class SubmissionPeriod(val start: LocalDateTime, val end: LocalDateTime) {
    SEMESTER_1(
        start = LocalDateTime.of(LocalDateTime.now().year, Month.AUGUST, 1, 0, 0),
        end = LocalDateTime.of(LocalDateTime.now().year, Month.AUGUST, 31, 23, 59)
    ),
    SEMESTER_2(
        start = LocalDateTime.of(LocalDateTime.now().year, Month.MARCH, 1, 0, 0),
        end = LocalDateTime.of(LocalDateTime.now().year, Month.DECEMBER, 31, 23, 59)
    );

    companion object {
        fun getCurrentPeriod(): SubmissionPeriod {
            val now = LocalDateTime.now()
            return entries.firstOrNull {
                (now.isEqual(it.start) || now.isEqual(it.end)) ||
                        (now.isAfter(it.start) && now.isBefore(it.end))
            } ?: throw NoActiveSubmissionPeriodException("Nenhum período de submissão está ativo no momento.")
        }

        fun getLastOpenPeriod(): Pair<LocalDateTime, LocalDateTime> {
            val now = LocalDateTime.now()
            val currentYear = now.year

            if (isWithinSubmissionPeriod()) {
                getCurrentPeriod().also {
                    return Pair(it.start, it.end)
                }
            } else {
                return if (now.month < Month.MARCH) {
                    Pair(
                        LocalDateTime.of(currentYear - 1, Month.AUGUST, 1, 0, 0),
                        LocalDateTime.of(currentYear - 1, Month.AUGUST, 31, 23, 59)
                    )
                } else if (now.month < Month.AUGUST) {
                    Pair(SEMESTER_2.start, SEMESTER_2.end)
                } else {
                    Pair(SEMESTER_1.start, SEMESTER_1.end)
                }
            }
        }

        fun isWithinSubmissionPeriod(): Boolean {
            return try {
                SubmissionPeriod.getCurrentPeriod()
                true
            } catch (e: NoActiveSubmissionPeriodException) {
                false
            }
        }
    }
}

class NoActiveSubmissionPeriodException(message: String) : RuntimeException(message)