package _4.fortuna_bff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.web.SecurityFilterChain
import _4.fortuna_bff.model.User

@Configuration
@EnableWebSecurity
class EnableGoogleAuth {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests { it.
                requestMatchers("/login/oauth2/code/google")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .requestMatchers("/signup")
                    .hasRole(User.UserRole.UNKNOWN.name)
            }
            .oauth2Login {
                it.userInfoEndpoint {
                    OidcUserService()
                }
            }

        return http.build()
    }
}
