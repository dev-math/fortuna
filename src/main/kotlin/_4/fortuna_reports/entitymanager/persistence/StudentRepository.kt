package _4.fortuna_reports.entitymanager.persistence

import _4.fortuna_reports.entitymanager.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository: JpaRepository<Student, Long>