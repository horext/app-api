package io.octatec.horext.api.model

import java.sql.Timestamp
import javax.persistence.*


@Entity
class HourlyLoad {

    var publishedAt: Timestamp? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_period_organization_unit_id", referencedColumnName = "id")
     var academicPeriodOrganizationUnit: AcademicPeriodOrganizationUnit? = null

    @OneToMany(mappedBy = "hourlyLoad", fetch = FetchType.LAZY)
     var scheduleSubjects: List<ScheduleSubject>? =   ArrayList()

     var checkedAt: Timestamp? = null

    constructor(id: Long?, publishedAt: Timestamp?) {
        this.publishedAt = publishedAt
        this.id = id
    }


    constructor(
        publishedAt: Timestamp?,
        id: Long?,
        academicPeriodOrganizationUnit: AcademicPeriodOrganizationUnit?,
        scheduleSubjects: List<ScheduleSubject>?,
        checkedAt: Timestamp?
    ) {
        this.publishedAt = publishedAt
        this.id = id
        this.academicPeriodOrganizationUnit = academicPeriodOrganizationUnit
        this.scheduleSubjects = scheduleSubjects
        this.checkedAt = checkedAt
    }



    constructor(id: Long?) {
        this.id = id
    }




}
