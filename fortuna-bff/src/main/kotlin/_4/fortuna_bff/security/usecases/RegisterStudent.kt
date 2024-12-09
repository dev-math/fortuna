package _4.fortuna_bff.security.usecases

import _4.fortuna_bff.core.Request
import _4.fortuna_bff.core.Student
import _4.fortuna_bff.core.UseCase
import _4.fortuna_bff.core.User
import _4.fortuna_bff.core.persistence.StudentRepository
import _4.fortuna_bff.core.persistence.UserRepository
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import io.konform.validation.jsonschema.minLength
import java.lang.RuntimeException

class RegisterStudent(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository
): UseCase<RegisterStudent.Request, Student>, Request<RegisterStudent.Request> {
    override fun execute(request: Request): Student {
        if (userRepository.existsByUspNumber(request.uspNumber)) {
            throw UserUspNumberAlreadyInUseException("O número USP informado já se encontra em uso, caso isso seja um erro entre em contato com um professor ou membro da CCP.")
        }

        val user = userRepository.findByEmail(request.email)!!.apply {
            uspNumber = request.uspNumber
            roles.clear()
            roles.add(User.Role.STUDENT)
        }

        userRepository.save(user)

        return studentRepository.save(
            Student(
                email = request.email,
                course = request.course,
                advisor = request.advisor,
                lattesProfile = request.lattesProfile,
                user = user,
            ),
        )
    }

    override fun validate(request: Request) = Validation {
        Request::course {
            pattern("Mestrado|Doutorado") hint "O curso deve ser 'Mestrado' ou 'Doutorado'."
        }
        Request::advisor {
            minLength(3) hint "O nome do orientador é obrigatório."
        }
        Request::lattesProfile {
            pattern("^(http|https)://.*$") hint "O link do currículo Lattes deve ser uma URL válida."
        }
        Request::uspNumber {
            pattern("^(?!0)\\d+$")
        }
    }.validate(request)

    data class Request(
        var email: String = "",
        val uspNumber: String,
        val advisor: String,
        val course: String,
        val lattesProfile: String,
    )

    class UserUspNumberAlreadyInUseException(e: String): RuntimeException(e)
}

