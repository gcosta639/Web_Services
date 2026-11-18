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
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.repository.ProjectRepository
import br.puc.ProjetoFinal.repository.TagRepository
import br.puc.ProjetoFinal.repository.TaskRepository
import br.puc.ProjetoFinal.repository.UserRepository
import br.puc.ProjetoFinal.security.AuthUser
import br.puc.ProjetoFinal.security.isAdmin
import br.puc.ProjetoFinal.security.requireSelfOrAdmin
import jakarta.persistence.criteria.Predicate
import org.slf4j.LoggerFactory
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository,
    private val projectRepository: ProjectRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(userId: Long, request: TaskRequest, auth: AuthUser): TaskResponse {
        auth.requireSelfOrAdmin(userId)

        val user = userRepository.findById(userId).orElseThrow {
            ResourceNotFoundException("Usuário com id $userId não encontrado")
        }
        val project = request.projectId?.let {
            projectRepository.findById(it).orElseThrow {
                ResourceNotFoundException("Projeto com id $it não encontrado")
            }
        }
        val task = Task(
            title = request.title,
            description = request.description,
            status = request.status,
            priority = request.priority,
            dueDate = request.dueDate,
            user = user,
            project = project
        )
        val saved = taskRepository.save(task)
        log.info("Tarefa criada: id={} para usuário {}", saved.id, userId)
        return saved.toResponse()
    }

    // USER vê só as próprias tarefas; ADMIN vê todas
    @Transactional(readOnly = true)
    fun findAll(status: TaskStatus?, priority: TaskPriority?, auth: AuthUser): List<TaskResponse> {
        val spec = Specification<Task> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()
            if (!auth.isAdmin()) {
                predicates.add(cb.equal(root.get<User>("user").get<Long>("id"), auth.id))
            }
            status?.let { predicates.add(cb.equal(root.get<TaskStatus>("status"), it)) }
            priority?.let { predicates.add(cb.equal(root.get<TaskPriority>("priority"), it)) }
            cb.and(*predicates.toTypedArray())
        }
        return taskRepository.findAll(spec).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun findById(id: Long, auth: AuthUser): TaskResponse = getOwnedTask(id, auth).toResponse()

    @Transactional
    fun delete(id: Long, auth: AuthUser) {
        taskRepository.delete(getOwnedTask(id, auth))
        log.info("Tarefa removida: id={}", id)
    }

    @Transactional
    fun addTag(taskId: Long, tagId: Long, auth: AuthUser): TaskResponse {
        val task = getOwnedTask(taskId, auth)
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
    fun removeTag(taskId: Long, tagId: Long, auth: AuthUser) {
        val task = getOwnedTask(taskId, auth)
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

    // Controle de propriedade: só o dono da tarefa ou um ADMIN
    private fun getOwnedTask(id: Long, auth: AuthUser): Task {
        val task = getTask(id)
        if (!auth.isAdmin() && task.user.id != auth.id) {
            throw AccessDeniedException("Usuário ${auth.id} não é dono da tarefa $id")
        }
        return task
    }
}