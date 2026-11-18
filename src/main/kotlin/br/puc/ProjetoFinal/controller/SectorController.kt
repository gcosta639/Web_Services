package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.SectorRequest
import br.puc.ProjetoFinal.dto.SectorResponse
import br.puc.ProjetoFinal.service.SectorService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/sectors")
class SectorController(private val sectorService: SectorService) {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(@Valid @RequestBody request: SectorRequest): ResponseEntity<SectorResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(sectorService.create(request))

    @GetMapping
    fun findAll(): List<SectorResponse> = sectorService.findAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): SectorResponse = sectorService.findById(id)

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(@PathVariable id: Long, @Valid @RequestBody request: SectorRequest): SectorResponse =
        sectorService.update(id, request)

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = sectorService.delete(id)
}