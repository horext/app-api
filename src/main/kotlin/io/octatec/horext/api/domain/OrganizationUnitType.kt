package io.octatec.horext.api.domain

data class OrganizationUnitType(
    val id: Long,
    val name: String?,
) {
    constructor(id: Long) : this(id, null)
}

enum class OrganizationUnitTypeCode(
    val id: Long,
) {
    FACULTY(2L),
    SPECIALITY(3L),
}
