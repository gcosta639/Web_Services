package br.puc.ProjetoFinal.security

import br.puc.ProjetoFinal.model.Role
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

// Usuário autenticado que fica disponível no SecurityContext
class AuthUser(
    val id: Long,
    private val email: String,
    private val passwordHash: String,
    val role: Role
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = email
}

fun AuthUser.isAdmin(): Boolean = role == Role.ADMIN

// Controle de propriedade: só o próprio usuário ou um ADMIN
fun AuthUser.requireSelfOrAdmin(userId: Long) {
    if (!isAdmin() && id != userId) {
        throw AccessDeniedException("Acesso negado ao usuário $userId")
    }
}