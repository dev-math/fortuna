package _4.fortuna_bff.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import _4.fortuna_bff.services.OidcUserService
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler

@Configuration
@EnableWebSecurity
class EnableGoogleAuth {
    @Bean
      fun filterChain(http: HttpSecurity, oidcUserService: OidcUserService): SecurityFilterChain {
        http
            .csrf {
                it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                it.csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
            }
            .oauth2Login {
                it.userInfoEndpoint {
                    it.oidcUserService(oidcUserService)
                }
                it.defaultSuccessUrl("http://localhost:4200/signup", true)
            }
            .authorizeRequests { it
                .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .requestMatchers("/login/**", "/oauth2/**", "/error", "/h2-console").permitAll()
                .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }

        return http.build()
    }
}
