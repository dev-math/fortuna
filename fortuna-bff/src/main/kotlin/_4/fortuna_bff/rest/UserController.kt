package _4.fortuna_bff.rest

import _4.fortuna_bff.core.Student
import _4.fortuna_bff.reports.SubmissionPeriod
import _4.fortuna_bff.security.CustomOidcUser
import _4.fortuna_bff.security.usecases.RegisterStudent
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val registerStudent: RegisterStudent
) {
    @PreAuthorize("hasRole('UNKNOWN')")
    @PostMapping("/signup")
    fun signup(@RequestBody request: RegisterStudent.Request): Student {
        return registerStudent.execute(request)
    }

    @GetMapping("/me")
    fun loggedUserDetails(@AuthenticationPrincipal loggedUser: CustomOidcUser): Any {
        return object {
            val name = loggedUser.name
            val email = loggedUser.email
            val roles = loggedUser.authorities.map { it.authority }
        }
    }

    @GetMapping("/submission")
    fun getSubmissionPeriod(): Boolean {
        return SubmissionPeriod.isWithinSubmissionPeriod()
    }
}