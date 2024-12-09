package _4.fortuna_bff.security.config

import _4.fortuna_bff.security.OidcUserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler

@Configuration
@EnableWebSecurity
class HttpConfig {

    @Bean
    fun filterChain(http: HttpSecurity, oidcUserService: OidcUserService): SecurityFilterChain {
        val csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse()

        http
            .csrf {
                it.csrfTokenRepository(csrfTokenRepository)
                it.csrfTokenRequestHandler(CsrfTokenRequestAttributeHandler())
            }
            .oauth2Login {
                it.userInfoEndpoint {
                    it.oidcUserService(oidcUserService)
                }
                it.successHandler { request, response, authentication ->
                    val csrfToken = request.getAttribute(CsrfToken::class.java.name) as? CsrfToken
                    if (csrfToken != null) {
                        csrfTokenRepository.saveToken(csrfToken, request, response)
                    }
                    response.sendRedirect("http://localhost:4200/signup")
                }
            }
            .authorizeRequests { it
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/login/**", "/oauth2/**", "/error", "/h2-console").permitAll()
                .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            }

        return http.build()
    }
}
