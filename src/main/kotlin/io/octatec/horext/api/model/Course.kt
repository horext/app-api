package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class Course  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: String? = null
    var name: String? = null

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    var subjects: List<Subject>? = null


    constructor(id: String?, name: String?) {
        this.id = id
        this.name = name
    }

    constructor() {}
}
