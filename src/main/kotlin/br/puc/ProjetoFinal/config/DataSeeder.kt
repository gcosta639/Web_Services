package br.puc.ProjetoFinal.config

import br.puc.ProjetoFinal.model.Project
import br.puc.ProjetoFinal.model.Role
import br.puc.ProjetoFinal.model.Sector
import br.puc.ProjetoFinal.model.Tag
import br.puc.ProjetoFinal.model.User
import br.puc.ProjetoFinal.model.UserProfile
import br.puc.ProjetoFinal.repository.ProjectRepository
import br.puc.ProjetoFinal.repository.SectorRepository
import br.puc.ProjetoFinal.repository.TagRepository
import br.puc.ProjetoFinal.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataSeeder(
    private val userRepository: UserRepository,
    private val tagRepository: TagRepository,
    private val sectorRepository: SectorRepository,
    private val projectRepository: ProjectRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun run(vararg args: String?) {
        // Setores
        listOf("Tecnologia", "Recursos Humanos", "Financeiro")
            .filterNot { sectorRepository.existsByName(it) }
            .forEach {
                val sector = sectorRepository.save(Sector(name = it))
                log.info("Seeder: setor '{}' criado (id={})", sector.name, sector.id)
            }
        val tecnologia = sectorRepository.findAll().first { it.name == "Tecnologia" }

        // Projeto de exemplo
        if (projectRepository.count() == 0L) {
            val project = projectRepository.save(
                Project(name = "Projeto Piloto", description = "Projeto de demonstração", sector = tecnologia)
            )
            log.info("Seeder: projeto '{}' criado (id={})", project.name, project.id)
        }

        // Usuários (senhas gravadas com BCrypt)
        if (userRepository.count() == 0L) {
            val teste = User(
                name = "Usuário Teste",
                email = "teste@email.com",
                password = passwordEncoder.encode("Teste@123"),
                role = Role.USER,
                sector = tecnologia
            )
            teste.profile = UserProfile(
                bio = "Usuário criado automaticamente para testes",
                phone = "(41) 99999-9999",
                user = teste
            )
            val admin = User(
                name = "Administrador",
                email = "admin@email.com",
                password = passwordEncoder.encode("Admin@123"),
                role = Role.ADMIN,
                sector = tecnologia
            )
            admin.profile = UserProfile(bio = "Administrador do sistema", user = admin)

            userRepository.save(teste)
            userRepository.save(admin)
            log.info("Seeder: usuários teste@email.com (USER) e admin@email.com (ADMIN) criados")
        }

        // Tags
        listOf("Trabalho", "Estudo", "Pessoal", "Urgente")
            .filterNot { tagRepository.existsByName(it) }
            .forEach {
                val tag = tagRepository.save(Tag(name = it))
                log.info("Seeder: tag '{}' criada (id={})", tag.name, tag.id)
            }
    }
}