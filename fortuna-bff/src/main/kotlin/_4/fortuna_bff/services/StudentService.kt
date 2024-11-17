package _4.fortuna_bff.services

import _4.fortuna_bff.model.CustomOidcUser
import _4.fortuna_bff.model.StudentDetails
import _4.fortuna_bff.model.User
import _4.fortuna_bff.util.Request
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service

@Service
class StudentService(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val oidcUserService: OidcUserService,
    private val clientRegistrationRepository: ClientRegistrationRepository,
) {
    @Transactional
    fun registerStudent(request: RegisterStudentRequest): StudentDetails {
        if (request.role != User.UserRole.STUDENT) {
            throw RuntimeException("nao pensei nesse fluxo ainda")
        }

        if (userRepository.existsByUspNumber(request.uspNumber)) {
            throw RuntimeException("Número USP ou email já registrado. " +
                    "Caso isso seja um erro entre em contato com um administrador" +
                    " do sistema")
        }

        val authentication = SecurityContextHolder.getContext().authentication
        val loggedUser = authentication.principal as CustomOidcUser


        val user: User = userRepository.findByEmail(loggedUser.email)!!.apply {
            uspNumber = request.uspNumber
            roles.clear()
            roles.add(request.role)
        }

        userRepository.save(user)

        oidcUserService.refreshRolesInContext(user.roles)

        return studentRepository.save(StudentDetails(
            course = request.course,
            advisor = request.advisor,
            lattesProfile = request.lattesProfile,
            user = user,
        ))
    }

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
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
