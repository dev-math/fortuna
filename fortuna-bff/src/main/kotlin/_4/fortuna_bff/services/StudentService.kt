package _4.fortuna_bff.services

import _4.fortuna_bff.model.CustomOidcUser
import _4.fortuna_bff.model.StudentDetails
import _4.fortuna_bff.model.User
import _4.fortuna_bff.util.Request
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class RegisterStudentDetails(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
) {
    fun registerStudent(request: RegisterStudentRequest): StudentDetails {
        if (request.role != User.UserRole.STUDENT) {
            throw RuntimeException("nao pensei nesse fluxo ainda")
        }

        val loggedUser = SecurityContextHolder.getContext()
            .authentication.principal as CustomOidcUser

        val user = User(
            uspNumber = request.uspNumber,
            email = loggedUser.email,
            name = loggedUser.name,
            roles = listOf(request.role)
        )

        if (userRepository.existsByUspNumber(request.uspNumber)) {
            throw RuntimeException("Número USP ou email já registrado. " +
                    "Caso isso seja um erro entre em contato com um administrador" +
                    " do sistema")
        }

        userRepository.save(user)
        return studentRepository.save(StudentDetails(
            course = request.course,
            advisor = request.advisor,
            lattesProfile = request.lattesProfile,
            user = user
        ))
    }

    data class RegisterStudentRequest(
        val uspNumber: String,
        val role: User.UserRole,
        val course: String,
        val advisor: String,
        val lattesProfile: String,
    ): Request<RegisterStudentRequest> {
        override fun validate() = Validation {
            RegisterStudentRequest::course {
                pattern("Mestrado|Doutorado") hint "O curso deve ser 'Mestrado' ou 'Doutorado'."
            }
            RegisterStudentRequest::advisor {
                minLength(3) hint "O nome do orientador é obrigatório."
            }
            RegisterStudentRequest::lattesProfile {
                pattern("^(http|https)://.*$") hint "O link do currículo Lattes deve ser uma URL válida."
            }
            RegisterStudentRequest::uspNumber {
                pattern("^(?!0)\\d+$")
            }
        }.validate(this)
    }
}