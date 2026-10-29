package br.puc.ProjetoFinal.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "tasks")
class Task(
    @Column(nullable = false)
    var title: String,

    @Column(length = 1000)
    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: TaskStatus = TaskStatus.PENDING,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var priority: TaskPriority = TaskPriority.MEDIUM,

    var dueDate: LocalDate? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {
    // Many-to-Many via tabela intermediária explícita (TaskTag)
    @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], orphanRemoval = true)
    var taskTags: MutableSet<TaskTag> = mutableSetOf()
}

