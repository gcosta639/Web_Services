package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.TagResponse
import br.puc.ProjetoFinal.service.TagService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tags")
class TagController(private val tagService: TagService) {

    @GetMapping
    fun findAll(): List<TagResponse> = tagService.findAll()
}