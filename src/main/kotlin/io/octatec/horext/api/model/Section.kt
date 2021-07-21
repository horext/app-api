package io.octatec.horext.api.model

import javax.persistence.*


@Entity
@Table(name = "section")
class Section {
    @Id
    var id: String? = null

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    var schedules: List<Schedule>? = null

    @ManyToMany
    @JoinTable(name = "section_organization_unit", joinColumns = [JoinColumn(name = "section_id")], inverseJoinColumns = [JoinColumn(name = "organization_unit_id")])
    var organizationUnits: List<OrganizationUnit>? = null
}
