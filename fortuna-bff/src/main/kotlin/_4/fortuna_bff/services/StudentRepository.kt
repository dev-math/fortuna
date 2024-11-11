package _4.fortuna_bff.services

import _4.fortuna_bff.model.StudentDetails
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<StudentDetails, String>