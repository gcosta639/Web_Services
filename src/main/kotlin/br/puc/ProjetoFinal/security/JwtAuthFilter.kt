package br.puc.ProjetoFinal.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

// Roda uma vez por requisição, antes dos controllers
@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader("Authorization")

        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)

            if (jwtService.isValid(token) && SecurityContextHolder.getContext().authentication == null) {
                try {
                    val user = userDetailsService.loadUserByUsername(jwtService.extractEmail(token))
                    val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                } catch (e: UsernameNotFoundException) {
                    // Usuário do token não existe mais: segue sem autenticar (resultará em 401)
                    logger.warn("Token de usuário inexistente: ${e.message}")
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}

