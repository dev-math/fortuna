package _4.fortuna_bff.rest

import _4.fortuna_bff.model.StudentDetails
import org.springframework.web.bind.annotation.PostMapping
import _4.fortuna_bff.services.RegisterStudentDetails
import org.springframework.web.bind.annotation.RestController

@RestController
class StudentController(
	private val registerStudentDetails: RegisterStudentDetails,
) {
	@PostMapping("/signup")
	fun registerStudent(request: RegisterStudentDetails.RegisterStudentRequest): StudentDetails {
		request.validateAndThrowOnError()
		return registerStudentDetails.registerStudent(request)
	}
}