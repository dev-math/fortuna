package _4.fortuna_bff

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.user.OidcUser

class CustomOidcUser(
    private val oidcUser: OidcUser,
    private val authorities: Collection<GrantedAuthority>,
) : OidcUser {
    override fun getName() = oidcUser.name

    override fun getAttributes() = oidcUser.attributes

    override fun getAuthorities() = authorities

    override fun getClaims() = oidcUser.claims

    override fun getUserInfo() = oidcUser.userInfo

    override fun getIdToken() = oidcUser.idToken
}
