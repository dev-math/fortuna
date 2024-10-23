package _4.fortuna_reports.entitymanager.usecases

import _4.fortuna_reports.entitymanager.Student
import _4.fortuna_reports.entitymanager.User
import _4.fortuna_reports.entitymanager.UserRole
import _4.fortuna_reports.entitymanager.exceptions.UserAlreadyExistsException
import _4.fortuna_reports.entitymanager.persistence.StudentRepository
import _4.fortuna_reports.entitymanager.persistence.UserRepository
import _4.fortuna_reports.utils.Request
import _4.fortuna_reports.utils.UseCase
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.*
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class RegisterUser(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
): UseCase<RegisterUserRequest, Student> {
    override fun execute(request: RegisterUserRequest): Student {
        val loggedUser = SecurityContextHolder.getContext()
            .authentication.principal as OidcUser

        val user = User(
            uspNumber = request.uspNumber,
            email = loggedUser.email,
            name = loggedUser.name,
            role = request.role
        )

        if (userRepository.existsByUspNumber(request.uspNumber)) {
            throw UserAlreadyExistsException("Número USP ou email já registrado. " +
                    "Caso isso seja um erro entre em contato com um administrador" +
                    " do sistema")
        }

        return when (request.role) {
            UserRole.STUDENT -> {
                userRepository.save(user)
                studentRepository.save(Student(
                    course = request.course,
                    advisor = request.advisor,
                    lattesProfile = request.lattesProfile,
                    user = user
                ))
            }
            else -> throw RuntimeException("n pensei nesse fluxo ainda")
        }
    }
}

data class RegisterUserRequest(
    val uspNumber: String,
    val role: UserRole,
    val course: String,
    val advisor: String,
    val lattesProfile: String,
): Request<RegisterUserRequest> {
    override fun validate(): ValidationResult<RegisterUserRequest> {
        return Validation<RegisterUserRequest> {
            RegisterUserRequest::course {
                pattern("Mestrado|Doutorado") hint "O curso deve ser 'Mestrado' ou 'Doutorado'."
            }
            RegisterUserRequest::advisor {
                minLength(3) hint "O nome do orientador é obrigatório."
            }
            RegisterUserRequest::lattesProfile {
                pattern("^(http|https)://.*$") hint "O link do currículo Lattes deve ser uma URL válida."
            }
            RegisterUserRequest::uspNumber {
                pattern("^\\d{8}$") hint "O número USP deve ser composto por 8 números."
            }
        }.validate(this)
    }
}
