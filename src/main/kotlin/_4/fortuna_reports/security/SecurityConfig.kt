package _4.fortuna_reports.security

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component

@Component
class SecurityConfig {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .authorizeHttpRequests{ authorize ->
                authorize
                    .requestMatchers("/login").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { login ->
j               login.defaultSuccessUrl("http://localhost:4200/additional-info")
            }
            .logout { logout ->
                logout.logoutSuccessUrl("/").permitAll()
            }
            .build()
    }
}
