package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class ClassSessionType {
    @Id
    var id: Long? = null
    var code: String? = null
    var name: String? = null

    @OneToMany(mappedBy = "classSessionType")
    var classSessions: List<ClassSession>? =  ArrayList()
}
