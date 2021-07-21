package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class EvaluationSystem {
    @Id
    var id: Long? = null

    @OneToMany(mappedBy = "evaluationSystem", fetch = FetchType.LAZY)
    var subjects: List<Subject> =  ArrayList()

    var name: String? = null
    var code: String? = null
}
