package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class ClassSessionType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var code: String? = null
    var name: String? = null

    @OneToMany(mappedBy = "classSessionType", fetch = FetchType.LAZY)
    var classSessions: List<ClassSession>? =  ArrayList()
}
