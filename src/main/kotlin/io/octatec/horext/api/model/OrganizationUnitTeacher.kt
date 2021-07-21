package io.octatec.horext.api.model

import java.sql.Timestamp
import javax.persistence.*

@Entity
class OrganizationUnitTeacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     var id: Long? = null

    @ManyToOne
    var organizationUnit: OrganizationUnit? = null

    @ManyToOne
    var teacher: Teacher? = null
    var fromDatetime: Timestamp? = null
    var toDatetime: Timestamp? = null
}
