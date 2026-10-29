package br.puc.ProjetoFinal.mapper

import br.puc.ProjetoFinal.dto.ProfileResponse
import br.puc.ProjetoFinal.dto.TagResponse
import br.puc.ProjetoFinal.dto.TaskResponse
import br.puc.ProjetoFinal.dto.UserResponse
import br.puc.ProjetoFinal.model.Tag
import br.puc.ProjetoFinal.model.Task
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile

fun User.toResponse() = UserResponse(
    id = id!!,
    name = name,
    email = email,
    bio = profile?.bio,
    phone = profile?.phone,
    createdAt = createdAt
)

fun UserProfile.toResponse() = ProfileResponse(
    userId = user.id!!,
    bio = bio,
    phone = phone
)

fun Tag.toResponse() = TagResponse(id = id!!, name = name)

// Deve ser chamado dentro de uma transação (acessa coleções lazy)
fun Task.toResponse() = TaskResponse(
    id = id!!,
    title = title,
    description = description,
    status = status,
    priority = priority,
    dueDate = dueDate,
    userId = user.id!!,
    tags = taskTags.map { it.tag.toResponse() }.sortedBy { it.name }
)
