package _4.fortuna_bff.security

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {
    fun getLoggedUser(): CustomOidcUser {
        return SecurityContextHolder.getContext().authentication.principal as CustomOidcUser
    }
}