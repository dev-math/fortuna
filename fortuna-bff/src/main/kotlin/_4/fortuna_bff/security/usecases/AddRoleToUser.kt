package _4.fortuna_bff.security.usecases

import _4.fortuna_bff.core.UseCase
import _4.fortuna_bff.core.User

class AddRoleToUser: UseCase<AddRoleToUser.Request, User> {
    data class Request(
        val email: String,
        val role: User.Role
    )

    override fun execute(request: Request): User {
        TODO("Not yet implemented")
    }
}