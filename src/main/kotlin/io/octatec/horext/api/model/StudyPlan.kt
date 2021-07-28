package io.octatec.horext.api.model

import java.util.*
import javax.persistence.*


@Entity
class StudyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(mappedBy = "studyPlan",fetch = FetchType.LAZY)
    var subjects: List<Subject> =  ArrayList()
    var name: String? = null
    var code: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var organizationUnit: OrganizationUnit = OrganizationUnit()
    var fromDate: Date? = null
    var toDate: Date? = null
}
