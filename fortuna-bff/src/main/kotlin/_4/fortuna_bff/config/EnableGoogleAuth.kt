package _4.fortuna_bff.config

import _4.fortuna_bff.model.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import _4.fortuna_bff.services.OidcUserService
import org.springframework.http.HttpStatus
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
class EnableGoogleAuth {
    @Bean
      fun filterChain(http: HttpSecurity, oidcUserService: OidcUserService): SecurityFilterChain {
        http
            .oauth2Login {
                it.userInfoEndpoint {
                    it.oidcUserService(oidcUserService)
                }
                it.defaultSuccessUrl("http://localhost:4200/signup", true)
            }
            .authorizeRequests { it
                .requestMatchers("/login/**", "/oauth2/**", "/error").permitAll()
                .requestMatchers("/signup").hasRole(User.UserRole.UNKNOWN.toString())
                .requestMatchers("/h2-console").permitAll()
                .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }

        return http.build()
    }
}
