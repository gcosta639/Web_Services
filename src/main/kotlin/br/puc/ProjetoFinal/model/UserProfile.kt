package br.puc.ProjetoFinal.model

import jakarta.persistence.*

@Entity
@Table(name = "user_profiles")
class UserProfile(
    @Column(length = 500)
    var bio: String? = null,

    var phone: String? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
)
