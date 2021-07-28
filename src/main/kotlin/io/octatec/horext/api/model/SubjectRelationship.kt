package io.octatec.horext.api.model

import javax.persistence.*

@Entity
class SubjectRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var fromSubject: Subject? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var toSubject: Subject? = null

    @ManyToOne
    var subjectRelationshipType: SubjectRelationshipType? = null
}
