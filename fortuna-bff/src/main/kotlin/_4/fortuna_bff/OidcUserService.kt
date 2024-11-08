package _4.fortuna_bff

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.stereotype.Service

@Service
class OidcUserService(
    private val userRepository: UserRepository,
) : OidcUserService() {

    override fun loadUser(request: OidcUserRequest): CustomOidcUser {
        val oidcUser = super.loadUser(request)

        if (!oidcUser.email.endsWith(USP_DOMAIN)) {
            logger.error("Invalid email for OidcUserRequest {}", request)
            throw OAuth2AuthenticationException("Entre com seu e-mail USP.")
        }

        val storedUser = userRepository.findByEmail(oidcUser.email)

        val user = User(
            name = oidcUser.name,
            email = oidcUser.email,
            roles = storedUser?.roles ?: listOf(User.UserRole.UNKNOWN),
        )

        if (storedUser == null) {
            logger.debug("Saving new user {}", user)
            userRepository.save(user)
        }

        val userAuthorities = oidcUser.authorities + userRolesToAuthorities(user.roles)

        logger.debug("Loading user {} with authorities {}", oidcUser, userAuthorities)
        return CustomOidcUser(oidcUser, userAuthorities)
    }

    private fun userRolesToAuthorities(roles: List<User.UserRole>): Collection<GrantedAuthority> {
        return roles.map {
            SimpleGrantedAuthority("ROLE_${it.name}")
        }
    }

    companion object {
        const val USP_DOMAIN = "@usp.br"
        val logger: Logger = LoggerFactory.getLogger(OidcUserService::class.java)
    }
}
