package br.puc.ProjetoFinal.model

import jakarta.persistence.*

@Entity
@Table(
    name = "task_tags",
    uniqueConstraints = [UniqueConstraint(columnNames = ["task_id", "tag_id"])]
)
class TaskTag(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    var task: Task,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    var tag: Tag,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
