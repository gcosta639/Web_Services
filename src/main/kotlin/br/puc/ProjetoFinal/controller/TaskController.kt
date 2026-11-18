package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.TaskRequest
import br.puc.ProjetoFinal.dto.TaskResponse
import br.puc.ProjetoFinal.model.TaskPriority
import br.puc.ProjetoFinal.model.TaskStatus
import br.puc.ProjetoFinal.security.AuthUser
import br.puc.ProjetoFinal.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
class TaskController(private val taskService: TaskService) {

    @PostMapping("/users/{userId}/tasks")
    fun create(
        @PathVariable userId: Long,
        @Valid @RequestBody request: TaskRequest,
        @AuthenticationPrincipal auth: AuthUser
    ): ResponseEntity<TaskResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(userId, request, auth))

    // GET /tasks?status=PENDING&priority=HIGH (ambos opcionais)
    @GetMapping("/tasks")
    fun findAll(
        @RequestParam(required = false) status: TaskStatus?,
        @RequestParam(required = false) priority: TaskPriority?,
        @AuthenticationPrincipal auth: AuthUser
    ): List<TaskResponse> = taskService.findAll(status, priority, auth)

    @GetMapping("/tasks/{id}")
    fun findById(@PathVariable id: Long, @AuthenticationPrincipal auth: AuthUser): TaskResponse =
        taskService.findById(id, auth)

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long, @AuthenticationPrincipal auth: AuthUser) =
        taskService.delete(id, auth)

    @PostMapping("/tasks/{taskId}/tags/{tagId}")
    fun addTag(
        @PathVariable taskId: Long,
        @PathVariable tagId: Long,
        @AuthenticationPrincipal auth: AuthUser
    ): TaskResponse = taskService.addTag(taskId, tagId, auth)

    @DeleteMapping("/tasks/{taskId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeTag(
        @PathVariable taskId: Long,
        @PathVariable tagId: Long,
        @AuthenticationPrincipal auth: AuthUser
    ) = taskService.removeTag(taskId, tagId, auth)
}