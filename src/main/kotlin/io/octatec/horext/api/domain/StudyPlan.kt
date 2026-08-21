package io.octatec.horext.api.domain

import java.time.Instant

data class StudyPlan(
    val id: Long,
    val code: String?,
    val name: String?,
    var fromDate: Instant?,
    var toDate: Instant?,
    var organizationUnit: OrganizationUnit?,
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
) {
    constructor(id: Long) : this(id, null, null, null, null, null)
}
