package io.octatec.horext.api.domain

import java.time.Instant

data class StudyPlan(
    val id: Long,
    val code: String?,
    var fromDate: Instant?,
    var toDate: Instant?,
    var organizationUnit: OrganizationUnit?,
) {
    constructor(id: Long) : this(id, null, null, null, null)
}
