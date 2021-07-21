package io.octatec.horext.api.model

import java.util.*
import javax.persistence.*


@Entity
class StudyPlan {
    @Id
    var id: Long? = null

    @OneToMany(mappedBy = "studyPlan",fetch = FetchType.LAZY)
    var subjects: List<Subject>? = null
    var name: String? = null
    var code: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var organizationUnit: OrganizationUnit? = null
    var fromDate: Date? = null
    var toDate: Date? = null
}
