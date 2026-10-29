package br.puc.ProjetoFinal.repository

import br.puc.ProjetoFinal.model.Tag
import br.puc.ProjetoFinal.model.Task
import br.puc.ProjetoFinal.model.TaskTag
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
}

interface UserProfileRepository : JpaRepository<UserProfile, Long>

// JpaSpecificationExecutor permite a filtragem dinâmica por status/priority
interface TaskRepository : JpaRepository<Task, Long>, JpaSpecificationExecutor<Task>

interface TagRepository : JpaRepository<Tag, Long> {
    fun existsByName(name: String): Boolean
}

interface TaskTagRepository : JpaRepository<TaskTag, Long>

