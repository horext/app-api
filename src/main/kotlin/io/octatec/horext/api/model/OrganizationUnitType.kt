package io.octatec.horext.api.model

import javax.persistence.*


@Entity
class OrganizationUnitType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var name: String?= null

    @OneToMany(mappedBy = "organizationUnitType", fetch = FetchType.LAZY)
    var organizationUnits: List<OrganizationUnit> = ArrayList()

    constructor(id: Long?) {
        this.id = id
    }

    constructor(id: Long?, name: String?, organizationUnits: List<OrganizationUnit>) {
        this.id = id
        this.name = name
        this.organizationUnits = organizationUnits
    }

    constructor()


}
