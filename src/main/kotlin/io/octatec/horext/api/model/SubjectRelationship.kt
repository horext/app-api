package io.octatec.horext.api.model

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class SubjectRelationship {
    @Id
    var id: Long? = null

    @ManyToOne(fetch = FetchType.EAGER)
    var fromSubject: Subject? = null

    @ManyToOne(fetch = FetchType.EAGER)
    var toSubject: Subject? = null

    @ManyToOne
    var subjectRelationshipType: SubjectRelationshipType? = null
}
