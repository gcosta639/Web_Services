package br.puc.ProjetoFinal.service

import br.puc.ProjetoFinal.dto.TaskRequest
import br.puc.ProjetoFinal.dto.TaskResponse
import br.puc.ProjetoFinal.exception.ConflictException
import br.puc.ProjetoFinal.exception.ResourceNotFoundException
import br.puc.ProjetoFinal.mapper.toResponse
import br.puc.ProjetoFinal.model.Task
import br.puc.ProjetoFinal.model.TaskPriority
import br.puc.ProjetoFinal.model.TaskStatus
import br.puc.ProjetoFinal.model.TaskTag
import br.puc.ProjetoFinal.repository.TagRepository
import br.puc.ProjetoFinal.repository.TaskRepository
import br.puc.ProjetoFinal.repository.UserRepository
import jakarta.persistence.criteria.Predicate
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(userId: Long, request: TaskRequest): TaskResponse {
        val user = userRepository.findById(userId).orElseThrow {
            ResourceNotFoundException("Usuário com id $userId não encontrado")
        }
        val task = Task(
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
            dueDate = request.dueDate,
            user = user
        )
        val saved = taskRepository.save(task)
        log.info("Tarefa criada: id={} para usuário {}", saved.id, userId)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(status: TaskStatus?, priority: TaskPriority?): List<TaskResponse> {
        val spec = Specification<Task> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            status?.let { predicates.add(cb.equal(root.get<TaskStatus>("status"), it)) }
            priority?.let { predicates.add(cb.equal(root.get<TaskPriority>("priority"), it)) }
            cb.and(*predicates.toTypedArray())
        }
        return taskRepository.findAll(spec).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long): TaskResponse = getTask(id).toResponse()

    @Transactional
    fun delete(id: Long) {
        taskRepository.delete(getTask(id))
        log.info("Tarefa removida: id={}", id)
    }

    @Transactional
    fun addTag(taskId: Long, tagId: Long): TaskResponse {
        val task = getTask(taskId)
        val tag = tagRepository.findById(tagId).orElseThrow {
            ResourceNotFoundException("Tag com id $tagId não encontrada")
        }
        if (task.taskTags.any { it.tag.id == tagId }) {
            throw ConflictException("A tarefa $taskId já possui a tag '${tag.name}'")
        }
        task.taskTags.add(TaskTag(task = task, tag = tag))
        log.info("Tag {} associada à tarefa {}", tagId, taskId)
        return taskRepository.save(task).toResponse()
    }

    @Transactional
    fun removeTag(taskId: Long, tagId: Long) {
        val task = getTask(taskId)
        val removed = task.taskTags.removeIf { it.tag.id == tagId }
        if (!removed) {
            throw ResourceNotFoundException("A tarefa $taskId não possui a tag $tagId")
        }
        log.info("Tag {} removida da tarefa {}", tagId, taskId)
    }

    private fun getTask(id: Long): Task =
        taskRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Tarefa com id $id não encontrada")
        }
}