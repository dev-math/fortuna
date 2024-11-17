package _4.fortuna_bff.rest

import _4.fortuna_bff.model.StudentDetails
import _4.fortuna_bff.services.StudentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import _4.fortuna_bff.services.StudentService.RegisterStudentRequest
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestBody

@RestController
class StudentController(
	private val studentService: StudentService,
) {
	@PreAuthorize("hasRole('UNKNOWN')")
	@PostMapping("/signup")
	fun registerStudent(@RequestBody request: RegisterStudentRequest): StudentDetails {
		request.validateAndThrowOnError()
		return studentService.registerStudent(request)
	}
}
