package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class SubjectRelationshipType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String? = null

    @OneToMany(mappedBy = "subjectRelationshipType")
    var subjectRelationships: List<SubjectRelationship>? = null
}
