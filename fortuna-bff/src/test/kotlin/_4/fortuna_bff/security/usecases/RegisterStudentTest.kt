package _4.fortuna_bff.security.usecases

import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class RegisterStudentTest {
    private val registerStudent = RegisterStudent(
        userRepository = mockk(relaxed = true),
        studentRepository = mockk(relaxed = true)
    )

    @Test
    fun `should fail validation when course is invalid`() {
        val invalidRequest = RegisterStudent.Request(
            email = "test@usp.br",
            uspNumber = "12345678",
            advisor = "Smith",
            course = "Graduação",
            lattesProfile = "https://lattes.cnpq.br/123456789",
        )

        val result = registerStudent.validate(invalidRequest)

        assertTrue(result.errors.any { it.dataPath.equals(".course") })
    }

    @Test
    fun `should fail validation when advisor name is too short`() {
        val invalidRequest = RegisterStudent.Request(
            email = "test@usp.br",
            uspNumber = "12345678",
            advisor = "h",
            course = "Mestrado",
            lattesProfile = "https://lattes.cnpq.br/123456789",
        )

        val result = registerStudent.validate(invalidRequest)

        assertTrue(result.errors.any { it.dataPath.equals(".advisor") })
    }

    @Test
    fun `should fail validation when lattesProfile is not a valid URL`() {
        val invalidRequest = RegisterStudent.Request(
            email = "test@usp.br",
            uspNumber = "12345678",
            advisor = "Milton Nascimento",
            course = "Mestrado",
            lattesProfile = "postgres://lattes.cnpq.br/123456789",
        )

        val result = registerStudent.validate(invalidRequest)

        assertTrue(result.errors.any { it.dataPath.equals(".lattesProfile") })
    }

    @Test
    fun `should fail validation when uspNumber is invalid`() {
        val invalidRequest = RegisterStudent.Request(
            email = "test@usp.br",
            uspNumber = "001023",
            advisor = "Milton Nascimento",
            course = "Mestrado",
            lattesProfile = "https://lattes.cnpq.br/123456789",
        )

        val result = registerStudent.validate(invalidRequest)

        assertTrue(result.errors.any { it.dataPath.equals(".uspNumber") })
    }
}