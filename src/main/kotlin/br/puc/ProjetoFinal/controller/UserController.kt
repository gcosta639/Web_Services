package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.ProfileRequest
import br.puc.ProjetoFinal.dto.ProfileResponse
import br.puc.ProjetoFinal.dto.UserRequest
import br.puc.ProjetoFinal.dto.UserResponse
import br.puc.ProjetoFinal.dto.UserUpdateRequest
import br.puc.ProjetoFinal.security.AuthUser
import br.puc.ProjetoFinal.security.requireSelfOrAdmin
import br.puc.ProjetoFinal.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    // Somente ADMIN cria usuários por aqui (o cadastro público é o /auth/register)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request))

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun findAll(): List<UserResponse> = userService.findAll()

    // Dados do usuário logado
    @GetMapping("/me")
    fun me(@AuthenticationPrincipal auth: AuthUser): UserResponse =
        userService.findById(auth.id)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long, @AuthenticationPrincipal auth: AuthUser): UserResponse {
        auth.requireSelfOrAdmin(id)
        return userService.findById(id)
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest,
        @AuthenticationPrincipal auth: AuthUser
    ): UserResponse {
        auth.requireSelfOrAdmin(id)
        return userService.update(id, request)
    }

    @PutMapping("/{id}/sector/{sectorId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun assignSector(@PathVariable id: Long, @PathVariable sectorId: Long): UserResponse =
        userService.assignSector(id, sectorId)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = userService.delete(id)

    @GetMapping("/{id}/profile")
    fun getProfile(@PathVariable id: Long, @AuthenticationPrincipal auth: AuthUser): ProfileResponse {
        auth.requireSelfOrAdmin(id)
        return userService.getProfile(id)
    }

    @PutMapping("/{id}/profile")
    fun updateProfile(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProfileRequest,
        @AuthenticationPrincipal auth: AuthUser
    ): ProfileResponse {
        auth.requireSelfOrAdmin(id)
        return userService.updateProfile(id, request)
    }
}