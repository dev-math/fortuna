package _4.fortuna_reports.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler


//Pode ser usado para direcionar baseado na role do user: Professor, CCP, Aluno
@Bean
fun customLoginSuccessHandler(): AuthenticationSuccessHandler {
    return AuthenticationSuccessHandler { request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication ->
        // Custom logic after login success
        // For example, logging the user's name:
        println("User ${authentication.name} logged in successfully")

        // Redirect to a custom page, for instance:
        response.sendRedirect("/welcome")
    }
}