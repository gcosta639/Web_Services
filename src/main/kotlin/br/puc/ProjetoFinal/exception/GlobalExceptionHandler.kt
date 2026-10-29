package br.puc.ProjetoFinal.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val details: List<String> = emptyList()
)

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        log.warn("Recurso não encontrado: {}", ex.message)
        return build(HttpStatus.NOT_FOUND, ex.message ?: "Recurso não encontrado")
    }

    @ExceptionHandler(ConflictException::class)
    fun handleConflict(ex: ConflictException): ResponseEntity<ErrorResponse> {
        log.warn("Conflito: {}", ex.message)
        return build(HttpStatus.CONFLICT, ex.message ?: "Conflito")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val details = ex.bindingResult.fieldErrors.map { "${it.field}: ${it.defaultMessage}" }
        log.warn("Erro de validação: {}", details)
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos", details)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        val msg = "Valor inválido '${ex.value}' para o parâmetro '${ex.name}'"
        log.warn(msg)
        return build(HttpStatus.BAD_REQUEST, msg)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadable(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        log.warn("Corpo da requisição ilegível: {}", ex.message)
        return build(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado")
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        log.error("Erro inesperado", ex)
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor")
    }

    private fun build(
        status: HttpStatus,
        message: String,
        details: List<String> = emptyList()
    ) = ResponseEntity.status(status).body(
        ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            details = details
        )
    )
}