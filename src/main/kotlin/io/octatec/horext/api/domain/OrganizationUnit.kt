package io.octatec.horext.api.domain

data class OrganizationUnit(
    var id: Long,
    var parentOrganizationUnit: OrganizationUnit?,
    var code: String?,
    var name: String?,
    var type: OrganizationUnitType?,
) {
    constructor(id: Long) : this(id, null, null, null, null)
    constructor(id: Long, code: String, name: String) : this(id, null, code, name, null)
}
