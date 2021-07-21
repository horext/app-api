package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Course  {
    @Id var id: String? = null
    var name: String? = null

    @OneToMany(mappedBy = "course")
    var subjects: List<Subject> =  ArrayList()

    constructor(id: String?, name: String?) {
        this.id = id
        this.name = name
    }

    constructor() {}
}
