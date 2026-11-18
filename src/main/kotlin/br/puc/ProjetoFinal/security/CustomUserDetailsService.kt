package br.puc.ProjetoFinal.security

import br.puc.ProjetoFinal.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Usuário não encontrado: $username")
        return AuthUser(
            id = user.id!!,
            email = user.email,
            passwordHash = user.password,
            role = user.role
        )
    }
}