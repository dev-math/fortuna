package _4.fortuna_reports.security

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser

@RestController
class AuthController {

    @GetMapping("/api/user/authenticated")
    fun isAuthenticated(@AuthenticationPrincipal user: OidcUser?): Boolean {
        return user != null
    }
}