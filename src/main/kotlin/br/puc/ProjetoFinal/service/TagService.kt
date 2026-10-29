package br.puc.ProjetoFinal.service

import br.puc.ProjetoFinal.dto.TagResponse
import br.puc.ProjetoFinal.mapper.toResponse
import br.puc.ProjetoFinal.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(private val tagRepository: TagRepository) {

    @Transactional(readOnly = true)
    fun findAll(): List<TagResponse> = tagRepository.findAll().map { it.toResponse() }
}