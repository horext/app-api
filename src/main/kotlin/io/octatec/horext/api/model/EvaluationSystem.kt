package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class EvaluationSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(mappedBy = "evaluationSystem", fetch = FetchType.LAZY)
    var subjects: List<Subject> =  ArrayList()

    var name: String? = null
    var code: String? = null
}
