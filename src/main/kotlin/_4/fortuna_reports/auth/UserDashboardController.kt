package _4.fortuna_reports.auth

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

class UserDashboardController {
//TODO: no futuro eh pra isso aqui redirecionar pra pagina pra terminar de colcoar as informações ou pra pagina de acesso de fato

    @GetMapping("/dashboard")
    fun dashboard(@AuthenticationPrincipal oidcUser: OidcUser, model: Model): String {
        model.addAttribute("user", oidcUser)
        return "dashboard" //view mockada pra um futuro dashboard T
    }
}