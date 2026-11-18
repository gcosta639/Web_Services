package br.puc.ProjetoFinal.mapper

import br.puc.ProjetoFinal.dto.ProfileResponse
import br.puc.ProjetoFinal.dto.ProjectResponse
import br.puc.ProjetoFinal.dto.SectorResponse
import br.puc.ProjetoFinal.dto.TagResponse
import br.puc.ProjetoFinal.dto.TaskResponse
import br.puc.ProjetoFinal.dto.UserResponse
import br.puc.ProjetoFinal.model.Project
import br.puc.ProjetoFinal.model.Sector
import br.puc.ProjetoFinal.model.Tag
import br.puc.ProjetoFinal.model.Task
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile

fun User.toResponse() = UserResponse(
    id = id!!,
    name = name,
    email = email,
    role = role,
    sectorId = sector?.id,
    sectorName = sector?.name,
    bio = profile?.bio,
    phone = profile?.phone,
    createdAt = createdAt
)

fun UserProfile.toResponse() = ProfileResponse(
    userId = user.id!!,
    bio = bio,
    phone = phone
)

fun Sector.toResponse() = SectorResponse(id = id!!, name = name, description = description)

fun Project.toResponse() = ProjectResponse(
    id = id!!,
    name = name,
    description = description,
    sectorId = sector.id!!,
    sectorName = sector.name
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
    projectId = project?.id,
    tags = taskTags.map { it.tag.toResponse() }.sortedBy { it.name }
)