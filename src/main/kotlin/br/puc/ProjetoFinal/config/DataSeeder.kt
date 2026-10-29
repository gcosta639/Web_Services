package br.puc.ProjetoFinal.config

import br.puc.ProjetoFinal.model.Tag
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile
import br.puc.ProjetoFinal.repository.TagRepository
import br.puc.ProjetoFinal.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            val user = User(name = "Usuário Teste", email = "teste@email.com")
            user.profile = UserProfile(
                bio = "Usuário criado automaticamente para testes",
                phone = "(41) 99999-9999",
                user = user
            )
            val saved = userRepository.save(user)
            log.info("Seeder: usuário de teste criado (id={})", saved.id)
        }

        listOf("Trabalho", "Estudo", "Pessoal", "Urgente")
            .filterNot { tagRepository.existsByName(it) }
            .forEach {
                val tag = tagRepository.save(Tag(name = it))
                log.info("Seeder: tag '{}' criada (id={})", tag.name, tag.id)
            }
    }
}