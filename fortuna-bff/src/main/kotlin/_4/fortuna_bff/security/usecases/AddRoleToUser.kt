package _4.fortuna_bff.security.usecases

import _4.fortuna_bff.core.UseCase
import _4.fortuna_bff.core.User
import _4.fortuna_bff.core.persistence.UserRepository

class AddRoleToUser(
    private val userRepository: UserRepository,
): UseCase<AddRoleToUser.Request, User> {
    data class Request(
        val email: String,
        val role: User.Role
    )

    override fun execute(request: Request): User {
        val user = userRepository.findByEmail(request.email)!!.apply {
            roles.remove(User.Role.UNKNOWN)
            roles.add(request.role)
        }

        return userRepository.save(user)
    }
}