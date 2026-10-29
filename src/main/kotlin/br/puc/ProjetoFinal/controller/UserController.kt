package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.ProfileRequest
import br.puc.ProjetoFinal.dto.ProfileResponse
import br.puc.ProjetoFinal.dto.UserRequest
import br.puc.ProjetoFinal.dto.UserResponse
import br.puc.ProjetoFinal.dto.UserUpdateRequest
import br.puc.ProjetoFinal.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    fun create(@Valid @RequestBody request: UserRequest): ResponseEntity<UserResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request))

    @GetMapping
    fun findAll(): List<UserResponse> = userService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): UserResponse = userService.findById(id)

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: UserUpdateRequest
    ): UserResponse = userService.update(id, request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = userService.delete(id)

    @GetMapping("/{id}/profile")
    fun getProfile(@PathVariable id: Long): ProfileResponse = userService.getProfile(id)

    @PutMapping("/{id}/profile")
    fun updateProfile(
        @PathVariable id: Long,
        @Valid @RequestBody request: ProfileRequest
    ): ProfileResponse = userService.updateProfile(id, request)
}