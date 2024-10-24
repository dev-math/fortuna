package _4.fortuna_reports.auth

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/login").permitAll()  // Allow public URLs
                    .anyRequest().authenticated()  // Require authentication for others
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/oauth2/authorization/google")
                    .successHandler(customLoginSuccessHandler())  // Custom success handler
                    .defaultSuccessUrl("/dashboard") //dashboard eh mock tem q mudar dps
                    .failureUrl("/login?error=true")
                    .userInfoEndpoint { userInfo ->
                        userInfo.oidcUserService(oidcUserService())
                    }
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/")  // Redirect after logout
            }

        return http.build()
    }

    @Bean
    fun oidcUserService(): OidcUserService {
        val delegate = OidcUserService()

        return object : OidcUserService() {
            override fun loadUser(userRequest: org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest): DefaultOidcUser {
                // Load the default OIDC user
                val oidcUser = delegate.loadUser(userRequest)

                // Get the existing authorities (roles from the OAuth provider)
                val authorities = oidcUser.authorities.toMutableSet()

                // Add custom authorities if needed
                authorities.add(SimpleGrantedAuthority("ROLE_USER")) // Example: assign a role

                // Return a new OidcUser with custom authorities
                return DefaultOidcUser(authorities, oidcUser.idToken, oidcUser.userInfo)
            }
        }
    }
}
