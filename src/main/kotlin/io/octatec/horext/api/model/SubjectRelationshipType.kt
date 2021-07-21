package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class SubjectRelationshipType {
    @Id
    var id: Long? = null
    var name: String? = null

    @OneToMany(mappedBy = "subjectRelationshipType")
    var subjectRelationships: List<SubjectRelationship>? = null
}
