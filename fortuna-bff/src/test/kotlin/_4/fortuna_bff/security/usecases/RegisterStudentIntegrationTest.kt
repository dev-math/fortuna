package _4.fortuna_bff.security.usecases

import _4.fortuna_bff.PostgresContainer
import _4.fortuna_bff.core.User
import _4.fortuna_bff.core.persistence.StudentRepository
import _4.fortuna_bff.core.persistence.UserRepository
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = [PostgresContainer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegisterStudentIntegrationTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var studentRepository: StudentRepository

    private lateinit var registerStudent: RegisterStudent

    @BeforeAll
    fun setup() {
        registerStudent = RegisterStudent(userRepository, studentRepository)
    }

    @BeforeEach
    fun cleanUp() {
        studentRepository.deleteAll()
        userRepository.deleteAll()
    }


    @Test
    fun `should register user as a student`() {
        val user = User(email = "student@usp.br", name = "student").apply {
            roles.add(User.Role.UNKNOWN)
        }.also {
            userRepository.save(it)
        }

        val request = RegisterStudent.Request(
            email = user.email,
            uspNumber = "12345678",
            advisor = "felipe smith",
            course = "Mestrado",
            lattesProfile = "https://lattes.cnpq.br/123456789",
        )

        registerStudent.execute(request).also {
            assertEquals(request.advisor, it.advisor)
            assertEquals(request.course, it.course)
            assertEquals(request.lattesProfile, it.lattesProfile)
            assertTrue(studentRepository.existsById(it.id))
        }

        userRepository.findByEmail(request.email)!!.also {
            assertEquals(mutableSetOf(User.Role.STUDENT), it.roles)
            assertEquals(request.uspNumber, it.uspNumber)
        }
    }

    @Test
    fun `should throw exception when USP number is already in use`() {
        userRepository.save(User(email="student@usp.br", name = "student", uspNumber = "12345678"))

        val user = User(email = "new-student@usp.br", name = "student").apply {
            roles.add(User.Role.UNKNOWN)
        }.also {
            userRepository.save(it)
        }

        val request = RegisterStudent.Request(
            email = user.email,
            uspNumber = "12345678",
            advisor = "akira presidente",
            course = "Mestrado",
            lattesProfile = "https://lattes.cnpq.br/987654321",
        )

        assertThrows<RegisterStudent.UserUspNumberAlreadyInUseException> {
            registerStudent.execute(request)
        }
    }
}
