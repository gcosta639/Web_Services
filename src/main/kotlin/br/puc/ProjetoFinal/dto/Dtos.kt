package br.puc.ProjetoFinal.dto

import br.puc.ProjetoFinal.model.TaskPriority
import br.puc.ProjetoFinal.model.TaskStatus
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

// ---------- User ----------
data class UserRequest(
    @field:NotBlank(message = "O nome é obrigatório")
    val name: String,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "E-mail inválido")
    val email: String,

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
    val dueDate: LocalDate? = null
)

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String?,
    val status: TaskStatus,
    val priority: TaskPriority,
    val dueDate: LocalDate?,
    val userId: Long,
    val tags: List<TagResponse>
)