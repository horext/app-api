package io.octatec.horext.api.model

import javax.persistence.*


@Entity
class OrganizationUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var code: String? = null

    var name: String? = null

    @ManyToOne( fetch = FetchType.LAZY)
    var organizationUnitType: OrganizationUnitType? = null

    @OneToMany(mappedBy = "organizationUnit", fetch = FetchType.LAZY)
     var organizationUnitTeachers: List<OrganizationUnitTeacher>? = ArrayList()

    @OneToMany(mappedBy = "organizationUnit", fetch = FetchType.LAZY)
     var studyPlans: List<StudyPlan>? = ArrayList()

    @OneToMany(mappedBy = "organizationUnit", fetch = FetchType.LAZY)
    var academicPeriodOrganizationUnits: List<AcademicPeriodOrganizationUnit>? = ArrayList()

    @ManyToMany(mappedBy = "organizationUnits", fetch = FetchType.LAZY)
     var sections: List<Section>? =  ArrayList()

    @ManyToOne( fetch = FetchType.LAZY)
     var parentOrganization: OrganizationUnit? = null

    @OneToMany(mappedBy = "parentOrganization", fetch = FetchType.LAZY)
    var childOrganizations: List<OrganizationUnit>? = ArrayList()
    
    constructor(id: Long?) {
        this.id = id
    }

    constructor(id: Long?, code: String?, name: String?) {
        this.id = id
        this.code = code
        this.name = name
    }

    constructor()

    constructor(id: Long?, code: String?, name: String?, organizationUnitType: OrganizationUnitType?) {
        this.id = id
        this.code = code
        this.name = name
        this.organizationUnitType = organizationUnitType
    }

    constructor(
        id: Long?,
        code: String?,
        name: String?,
        organizationUnitType: OrganizationUnitType?,
        organizationUnitTeachers: List<OrganizationUnitTeacher>?,
        studyPlans: List<StudyPlan>?,
        academicPeriodOrganizationUnits: List<AcademicPeriodOrganizationUnit>?,
        sections: List<Section>?,
        parentOrganization: OrganizationUnit?,
        childOrganizations: List<OrganizationUnit>?
    ) {
        this.id = id
        this.code = code
        this.name = name
        this.organizationUnitType = organizationUnitType
        this.organizationUnitTeachers = organizationUnitTeachers
        this.studyPlans = studyPlans
        this.academicPeriodOrganizationUnits = academicPeriodOrganizationUnits
        this.sections = sections
        this.parentOrganization = parentOrganization
        this.childOrganizations = childOrganizations
    }


}
