package _4.fortuna_bff.core.persistence

import _4.fortuna_bff.core.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, String>
