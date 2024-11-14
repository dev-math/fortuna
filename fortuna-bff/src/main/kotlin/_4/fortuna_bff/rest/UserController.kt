package _4.fortuna_bff.rest

import _4.fortuna_bff.model.CustomOidcUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
	@GetMapping("/me")
	fun loggedUserDetails(@AuthenticationPrincipal loggedUser: CustomOidcUser): Any {
		return object {
			val name = loggedUser.name
			val email = loggedUser.email
			val roles = loggedUser.authorities.map { it.authority }
		}
	}
}
