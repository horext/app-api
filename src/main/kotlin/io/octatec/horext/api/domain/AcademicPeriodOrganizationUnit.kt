package io.octatec.horext.api.domain

import java.time.Instant

data class AcademicPeriodOrganizationUnit(
    val id: Long,
    var fromDate: Instant?,
    var toDate: Instant?,
    var academicPeriod: AcademicPeriod?,
    var organizationUnit: OrganizationUnit?,
) {
    constructor(id: Long) : this(id, null, null, null, null)
}
