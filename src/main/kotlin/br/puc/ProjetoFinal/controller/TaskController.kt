package br.puc.ProjetoFinal.controller

import br.puc.ProjetoFinal.dto.TaskRequest
import br.puc.ProjetoFinal.dto.TaskResponse
import br.puc.ProjetoFinal.model.TaskPriority
import br.puc.ProjetoFinal.model.TaskStatus
import br.puc.ProjetoFinal.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class TaskController(private val taskService: TaskService) {

    @PostMapping("/users/{userId}/tasks")
    fun create(
        @PathVariable userId: Long,
        @Valid @RequestBody request: TaskRequest
    ): ResponseEntity<TaskResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(taskService.create(userId, request))

    // GET /tasks?status=PENDING&priority=HIGH (ambos opcionais)
    @GetMapping("/tasks")
    fun findAll(
        @RequestParam(required = false) status: TaskStatus?,
        @RequestParam(required = false) priority: TaskPriority?
    ): List<TaskResponse> = taskService.findAll(status, priority)

    @GetMapping("/tasks/{id}")
    fun findById(@PathVariable id: Long): TaskResponse = taskService.findById(id)

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = taskService.delete(id)

    @PostMapping("/tasks/{taskId}/tags/{tagId}")
    fun addTag(@PathVariable taskId: Long, @PathVariable tagId: Long): TaskResponse =
        taskService.addTag(taskId, tagId)

    @DeleteMapping("/tasks/{taskId}/tags/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeTag(@PathVariable taskId: Long, @PathVariable tagId: Long) =
        taskService.removeTag(taskId, tagId)
}