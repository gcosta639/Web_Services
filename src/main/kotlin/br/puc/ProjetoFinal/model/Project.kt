package br.puc.ProjetoFinal.model

import jakarta.persistence.*

@Entity
@Table(name = "projects")
class Project(
    @Column(nullable = false)
    var name: String,

    @Column(length = 1000)
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id", nullable = false)
    var sector: Sector,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    @OneToMany(mappedBy = "project")
    var tasks: MutableList<Task> = mutableListOf()
}
