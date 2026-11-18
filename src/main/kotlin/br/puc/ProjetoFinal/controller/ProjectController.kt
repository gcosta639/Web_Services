package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.ProjectRequest
import br.puc.ProjetoFinal.dto.ProjectResponse
import br.puc.ProjetoFinal.service.ProjectService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects")
class ProjectController(private val projectService: ProjectService) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@Valid @RequestBody request: ProjectRequest): ResponseEntity<ProjectResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request))

    // GET /projects?sectorId=1 (filtro opcional)
    @GetMapping
    fun findAll(@RequestParam(required = false) sectorId: Long?): List<ProjectResponse> =
        projectService.findAll(sectorId)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ProjectResponse = projectService.findById(id)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = projectService.delete(id)
}