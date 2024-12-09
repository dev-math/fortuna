package _4.fortuna_bff.security

import _4.fortuna_bff.core.User
import _4.fortuna_bff.core.persistence.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import kotlin.collections.union

@Service
class OidcUserService(
    private val userRepository: UserRepository,
): OidcUserService() {
    override fun loadUser(request: OidcUserRequest): OidcUser {
        val oidcUser = super.loadUser(request)

        if (!oidcUser.email.endsWith(USP_DOMAIN)) {
            logger.error("Invalid email for OidcUserRequest {}", request)
            throw OAuth2AuthenticationException("Entre com seu e-mail USP.")
        }

        val storedUser = userRepository.findByEmail(oidcUser.email)

        val user = User(
            name = oidcUser.name,
            email = oidcUser.email,
            roles = storedUser?.roles ?: mutableSetOf(User.Role.UNKNOWN),
        )

        if (storedUser == null) {
            logger.debug("Saving new user {}", user)
            userRepository.save(user)
        }

        val userAuthorities = oidcUser.authorities union userRolesToAuthorities(user.roles)

        logger.debug("Loading user {} with authorities {}", oidcUser, userAuthorities)

        return CustomOidcUser(oidcUser, userAuthorities)
    }

    fun refreshRolesInContext(roles: Set<User.Role>) {
        val authentication = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
        val user = authentication.principal as CustomOidcUser

        logger.info("Refreshing user {} roles {}", user.name, roles)

        val authorities = user.authorities.filterNot { it.authority.startsWith("ROLE_")} union userRolesToAuthorities(roles)

        authorities.forEach { logger.info("Autoridade {}", it.authority) }

        val updatedUser = CustomOidcUser(user, authorities)

        val newAuthentication = OAuth2AuthenticationToken(
            updatedUser,
            authorities,
            authentication.authorizedClientRegistrationId,
        )

        SecurityContextHolder.getContext().authentication = newAuthentication
    }

    private fun userRolesToAuthorities(roles: Set<User.Role>): Collection<GrantedAuthority> {
        return roles.map {
            SimpleGrantedAuthority("ROLE_${it.name}")
        }
    }

    companion object {
        const val USP_DOMAIN = "@usp.br"
        val logger: Logger = LoggerFactory.getLogger(OidcUserService::class.java)
    }
}
