package br.puc.ProjetoFinal.service

import br.puc.ProjetoFinal.dto.AuthResponse
import br.puc.ProjetoFinal.dto.LoginRequest
import br.puc.ProjetoFinal.dto.RegisterRequest
import br.puc.ProjetoFinal.dto.UserRequest
import br.puc.ProjetoFinal.model.Role
import br.puc.ProjetoFinal.security.AuthUser
import br.puc.ProjetoFinal.security.JwtService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    // O cadastro público sempre cria um USER: ninguém vira ADMIN pelo registro
    fun register(request: RegisterRequest): AuthResponse {
        val user = userService.create(
            UserRequest(
                name = request.name,
                email = request.email,
                password = request.password,
                role = Role.USER,
                sectorId = request.sectorId
            )
        )
        log.info("Usuário registrado: {}", user.email)
        return AuthResponse(
            token = jwtService.generateToken(user.email, user.role),
            userId = user.id,
            email = user.email,
            role = user.role
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        // Lança BadCredentialsException (vira 401) se e-mail ou senha estiverem errados
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val principal = authentication.principal as AuthUser
        log.info("Login realizado: {}", principal.username)
        return AuthResponse(
            token = jwtService.generateToken(principal.username, principal.role),
            userId = principal.id,
            email = principal.username,
            role = principal.role
        )
    }
}