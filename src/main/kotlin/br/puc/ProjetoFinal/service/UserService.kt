package br.puc.ProjetoFinal.service

import br.puc.ProjetoFinal.dto.ProfileRequest
import br.puc.ProjetoFinal.dto.ProfileResponse
import br.puc.ProjetoFinal.dto.UserRequest
import br.puc.ProjetoFinal.dto.UserResponse
import br.puc.ProjetoFinal.dto.UserUpdateRequest
import br.puc.ProjetoFinal.exception.ConflictException
import br.puc.ProjetoFinal.exception.ResourceNotFoundException
import br.puc.ProjetoFinal.mapper.toResponse
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile
import br.puc.ProjetoFinal.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun create(request: UserRequest): UserResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw ConflictException("Já existe um usuário com o e-mail ${request.email}")
        }
        val user = User(name = request.name, email = request.email)
        user.profile = UserProfile(bio = request.bio, phone = request.phone, user = user)

        val saved = userRepository.save(user)
        log.info("Usuário criado: id={}, email={}", saved.id, saved.email)
        return saved.toResponse()
    }

    @Transactional(readOnly = true)
    fun findAll(): List<UserResponse> = userRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun findById(id: Long): UserResponse = getUser(id).toResponse()

    @Transactional
    fun update(id: Long, request: UserUpdateRequest): UserResponse {
        val user = getUser(id)
        if (request.email != user.email && userRepository.existsByEmail(request.email)) {
            throw ConflictException("Já existe um usuário com o e-mail ${request.email}")
        }
        user.name = request.name
        user.email = request.email
        log.info("Usuário atualizado: id={}", id)
        return user.toResponse()
    }

    @Transactional
    fun delete(id: Long) {
        userRepository.delete(getUser(id))
        log.info("Usuário removido: id={}", id)
    }

    @Transactional(readOnly = true)
    fun getProfile(userId: Long): ProfileResponse {
        val profile = getUser(userId).profile
            ?: throw ResourceNotFoundException("Usuário $userId não possui perfil")
        return profile.toResponse()
    }

    @Transactional
    fun updateProfile(userId: Long, request: ProfileRequest): ProfileResponse {
        val user = getUser(userId)
        // Se por algum motivo não existir perfil, cria um (cascade ALL persiste junto)
        val profile = user.profile ?: UserProfile(user = user).also { user.profile = it }
        profile.bio = request.bio
        profile.phone = request.phone
        log.info("Perfil atualizado: userId={}", userId)
        return profile.toResponse()
    }

    private fun getUser(id: Long): User =
        userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Usuário com id $id não encontrado")
        }
}