package br.puc.ProjetoFinal.repository

import br.puc.ProjetoFinal.model.Project
import br.puc.ProjetoFinal.model.Sector
import br.puc.ProjetoFinal.model.Tag
import br.puc.ProjetoFinal.model.Task
import br.puc.ProjetoFinal.model.TaskTag
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface UserRepository : JpaRepository<User, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
    fun existsBySectorId(sectorId: Long): Boolean
}

interface UserProfileRepository : JpaRepository<UserProfile, Long>

interface SectorRepository : JpaRepository<Sector, Long> {
    fun existsByName(name: String): Boolean
}

interface ProjectRepository : JpaRepository<Project, Long> {
    fun existsBySectorId(sectorId: Long): Boolean
    fun findBySectorId(sectorId: Long): List<Project>
}

interface TaskRepository : JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    fun existsByProjectId(projectId: Long): Boolean
}

interface TagRepository : JpaRepository<Tag, Long> {
    fun existsByName(name: String): Boolean
}

interface TaskTagRepository : JpaRepository<TaskTag, Long>