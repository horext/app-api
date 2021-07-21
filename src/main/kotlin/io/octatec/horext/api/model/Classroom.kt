package io.octatec.horext.api.model

import javax.persistence.*


@Entity
class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var code: String? = null
    var name: String? = null

    @OneToMany(mappedBy = "classroom")
    var classSessions: List<ClassSession>? = null
}
