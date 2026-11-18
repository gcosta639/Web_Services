package br.puc.ProjetoFinal.service

import br.puc.ProjetoFinal.dto.SectorRequest
import br.puc.ProjetoFinal.dto.SectorResponse
import br.puc.ProjetoFinal.exception.ConflictException
import br.puc.ProjetoFinal.exception.ResourceNotFoundException
import br.puc.ProjetoFinal.mapper.toResponse
import br.puc.ProjetoFinal.model.Sector
import br.puc.ProjetoFinal.repository.ProjectRepository
import br.puc.ProjetoFinal.repository.SectorRepository
import br.puc.ProjetoFinal.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SectorService(
    private val sectorRepository: SectorRepository,
    private val userRepository: UserRepository,
    private val projectRepository: ProjectRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(request: SectorRequest): SectorResponse {
        if (sectorRepository.existsByName(request.name)) {
            throw ConflictException("Já existe um setor com o nome '${request.name}'")
        }
        val saved = sectorRepository.save(Sector(name = request.name, description = request.description))
        log.info("Setor criado: id={}", saved.id)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<SectorResponse> = sectorRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): SectorResponse = getSector(id).toResponse()

    @Transactional
    fun update(id: Long, request: SectorRequest): SectorResponse {
        val sector = getSector(id)
        if (request.name != sector.name && sectorRepository.existsByName(request.name)) {
            throw ConflictException("Já existe um setor com o nome '${request.name}'")
        }
        sector.name = request.name
        sector.description = request.description
        log.info("Setor atualizado: id={}", id)
        return sector.toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        val sector = getSector(id)
        if (userRepository.existsBySectorId(id) || projectRepository.existsBySectorId(id)) {
            throw ConflictException("Não é possível remover o setor: há usuários ou projetos vinculados")
        }
        sectorRepository.delete(sector)
        log.info("Setor removido: id={}", id)
    }

    private fun getSector(id: Long): Sector =
        sectorRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Setor com id $id não encontrado")
        }
}