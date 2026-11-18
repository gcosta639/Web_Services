package br.puc.ProjetoFinal.security

import br.puc.ProjetoFinal.exception.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

private fun writeError(
    response: HttpServletResponse,
    mapper: ObjectMapper,
    status: HttpStatus,
    message: String
) {
    response.status = status.value()
    response.contentType = MediaType.APPLICATION_JSON_VALUE
    response.characterEncoding = "UTF-8"
    mapper.writeValue(
        response.writer,
        ErrorResponse(status = status.value(), error = status.reasonPhrase, message = message)
    )
}

// 401: sem token ou token inválido (erro no nível do filtro)
@Component
class RestAuthenticationEntryPoint(private val mapper: ObjectMapper) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) = writeError(response, mapper, HttpStatus.UNAUTHORIZED, "Token ausente, inválido ou expirado")
}

// 403: autenticado, mas sem permissão (erro no nível do filtro)
@Component
class RestAccessDeniedHandler(private val mapper: ObjectMapper) : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) = writeError(response, mapper, HttpStatus.FORBIDDEN, "Você não tem permissão para acessar este recurso")
}
