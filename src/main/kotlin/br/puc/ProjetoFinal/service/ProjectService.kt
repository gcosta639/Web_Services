package br.puc.ProjetoFinal.service

import br.puc.ProjetoFinal.dto.ProjectRequest
import br.puc.ProjetoFinal.dto.ProjectResponse
import br.puc.ProjetoFinal.exception.ConflictException
import br.puc.ProjetoFinal.exception.ResourceNotFoundException
import br.puc.ProjetoFinal.mapper.toResponse
import br.puc.ProjetoFinal.model.Project
import br.puc.ProjetoFinal.repository.ProjectRepository
import br.puc.ProjetoFinal.repository.SectorRepository
import br.puc.ProjetoFinal.repository.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val sectorRepository: SectorRepository,
    private val taskRepository: TaskRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(request: ProjectRequest): ProjectResponse {
        val sectorId = request.sectorId!! // garantido pelo @NotNull do DTO
        val sector = sectorRepository.findById(sectorId).orElseThrow {
            ResourceNotFoundException("Setor com id $sectorId não encontrado")
        }
        val saved = projectRepository.save(
            Project(name = request.name, description = request.description, sector = sector)
        )
        log.info("Projeto criado: id={} no setor {}", saved.id, sectorId)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(sectorId: Long?): List<ProjectResponse> {
        val projects = if (sectorId != null) projectRepository.findBySectorId(sectorId)
        else projectRepository.findAll()
        return projects.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): ProjectResponse = getProject(id).toResponse()

    @Transactional
    fun delete(id: Long) {
        val project = getProject(id)
        if (taskRepository.existsByProjectId(id)) {
            throw ConflictException("Não é possível remover o projeto: há tarefas vinculadas")
        }
        projectRepository.delete(project)
        log.info("Projeto removido: id={}", id)
    }

    private fun getProject(id: Long): Project =
        projectRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Projeto com id $id não encontrado")
        }
}