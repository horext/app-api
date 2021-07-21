package io.octatec.horext.api.model

import java.util.*
import javax.persistence.*


@Entity
class AcademicPeriodOrganizationUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var academicPeriod: AcademicPeriod? = null

    @ManyToOne(fetch = FetchType.LAZY)
    var organizationUnit: OrganizationUnit? = null

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "academicPeriodOrganizationUnit")
    var hourlyLoad: HourlyLoad? = null

    var fromDate: Date? = null
    var toDate: Date? = null
}
