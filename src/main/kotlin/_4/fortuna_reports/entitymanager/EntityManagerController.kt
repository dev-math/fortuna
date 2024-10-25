import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EntityManagerController {
    @GetMapping("/api/me")
    fun getUserInfo(@AuthenticationPrincipal user: OidcUser): Map<String, String?> {
        return mapOf(
            "profileImage" to user.getAttribute("picture"),
            "role" to user.getAttribute("role") // ou outra forma de obter a role
        )
    }
}
