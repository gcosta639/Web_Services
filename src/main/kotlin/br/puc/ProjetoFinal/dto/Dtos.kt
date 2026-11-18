package br.puc.ProjetoFinal.dto

import br.puc.ProjetoFinal.model.Role
import br.puc.ProjetoFinal.model.TaskPriority
import br.puc.ProjetoFinal.model.TaskStatus
import br.puc.ProjetoFinal.validation.StrongPassword
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

// ---------- Auth ----------
data class RegisterRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    val name: String,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "E-mail inválido")
    val email: String,

    @field:StrongPassword
    val password: String,

    val sectorId: Long? = null
)

data class LoginRequest(
    @field:NotBlank(message = "O e-mail é obrigatório")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória")
    val password: String
)

data class AuthResponse(
    val token: String,
    val tokenType: String = "Bearer",
    val userId: Long,
    val email: String,
    val role: Role
)

// ---------- User ----------
data class UserRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    val name: String,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "E-mail inválido")
    val email: String,

    @field:StrongPassword
    val password: String,

    val role: Role = Role.USER,
    val sectorId: Long? = null,

    @field:Size(max = 500, message = "A bio deve ter no máximo 500 caracteres")
    val bio: String? = null,

    val phone: String? = null
)

data class UserUpdateRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    val name: String,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "E-mail inválido")
    val email: String
)

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: Role,
    val sectorId: Long?,
    val sectorName: String?,
    val bio: String?,
    val phone: String?,
    val createdAt: LocalDateTime
)

// ---------- Profile ----------
data class ProfileRequest(
    @field:Size(max = 500, message = "A bio deve ter no máximo 500 caracteres")
    val bio: String? = null,

    val phone: String? = null
)

data class ProfileResponse(
    val userId: Long,
    val bio: String?,
    val phone: String?
)

// ---------- Sector ----------
data class SectorRequest(
    @field:NotBlank(message = "O nome do setor é obrigatório")
    val name: String,

    @field:Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    val description: String? = null
)

data class SectorResponse(
    val id: Long,
    val name: String,
    val description: String?
)

// ---------- Project ----------
data class ProjectRequest(
    @field:NotBlank(message = "O nome do projeto é obrigatório")
    val name: String,

    @field:Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    val description: String? = null,

    @field:NotNull(message = "O setor é obrigatório")
    val sectorId: Long?
)

data class ProjectResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val sectorId: Long,
    val sectorName: String
)

// ---------- Tag ----------
data class TagResponse(
    val id: Long,
    val name: String
)

// ---------- Task ----------
data class TaskRequest(
    @field:NotBlank(message = "O título é obrigatório")
    val title: String,

    @field:Size(max = 1000, message = "A descrição deve ter no máximo 1000 caracteres")
    val description: String? = null,

    val status: TaskStatus = TaskStatus.PENDING,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: LocalDate? = null,
    val projectId: Long? = null
)

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val dueDate: LocalDate?,
    val userId: Long,
    val projectId: Long?,
    val tags: List<TagResponse>
)