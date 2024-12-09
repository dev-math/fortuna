package _4.fortuna_bff.security.usecases

import _4.fortuna_bff.PostgresContainer
import _4.fortuna_bff.core.User
import _4.fortuna_bff.core.persistence.UserRepository
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@ContextConfiguration(initializers = [PostgresContainer::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AddRoleToUserIntegrationTest {

    @Autowired
    lateinit var userRepository: UserRepository

    private lateinit var addRoleToUser: AddRoleToUser

    @BeforeAll
    fun setUp() {
        addRoleToUser = AddRoleToUser(userRepository)
    }

    @BeforeEach
    fun cleanUp() {
        userRepository.deleteAll()
    }

    @Test
    fun `should add a role to the user and remove UNKNOWN if present`() {
        val existingUser = User(
            email = "test@usp.br",
            name = "Test Student",
            roles = mutableSetOf(User.Role.UNKNOWN)
        ).also {
            userRepository.save(it)
        }

        val request = AddRoleToUser.Request(
            email = existingUser.email,
            role = User.Role.PROFESSOR
        )

        addRoleToUser.execute(request)

        userRepository.findByEmail(request.email)!!.also {
            assertTrue(it.roles.contains(User.Role.PROFESSOR))
            assertTrue(!it.roles.contains(User.Role.UNKNOWN))
        }
    }

    @Test
    fun `should add a new role to the user without removing existing roles`() {
        val existingUser = User(
            email = "test@usp.br",
            name = "João Nogueira",
            roles = mutableSetOf(User.Role.PROFESSOR)
        ).also {
            userRepository.save(it)
        }

        val request = AddRoleToUser.Request(
            email = existingUser.email,
            role = User.Role.CCP
        )

        addRoleToUser.execute(request)

        userRepository.findByEmail(request.email)!!.also {
            assertTrue(it.roles.contains(User.Role.PROFESSOR))
            assertTrue(it.roles.contains(User.Role.CCP))
        }
    }
}
