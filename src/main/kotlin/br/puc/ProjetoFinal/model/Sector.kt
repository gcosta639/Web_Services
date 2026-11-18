package br.puc.ProjetoFinal.model

import jakarta.persistence.*

@Entity
@Table(name = "sectors")
class Sector(
    @Column(nullable = false, unique = true)
    var name: String,

    @Column(length = 500)
    var description: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    @OneToMany(mappedBy = "sector")
    var users: MutableList<User> = mutableListOf()

    @OneToMany(mappedBy = "sector")
    var projects: MutableList<Project> = mutableListOf()
}