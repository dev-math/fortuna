package _4.fortuna_bff.rest

import _4.fortuna_bff.security.usecases.AddRoleToUser
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@PreAuthorize("hasRole('ADMIN')")
class AdminController(
    private val addRoleToUser: AddRoleToUser
) {
    @PostMapping("/admin/user")
    fun addRoleToUser(@RequestBody request: AddRoleToUser.Request) {
        addRoleToUser.execute(request)
    }
}