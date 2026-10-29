package br.puc.ProjetoFinal.model

import jakarta.persistence.*

@Entity
@Table(name = "tags")
class Tag(
    @Column(nullable = false, unique = true)
    var name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    @OneToMany(mappedBy = "tag", cascade = [CascadeType.REMOVE])
    var taskTags: MutableSet<TaskTag> = mutableSetOf()
}