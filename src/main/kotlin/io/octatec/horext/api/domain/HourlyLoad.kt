package io.octatec.horext.api.domain

import java.time.Instant

data class HourlyLoad(
    val id: Long,
    var name: String,
    var checkedAt: Instant?,
    var updatedAt: Instant?,
    var publishedAt: Instant?,
    var academicPeriodOrganizationUnit: AcademicPeriodOrganizationUnit?,
) {
    constructor(id: Long) : this(id, "", null, null, null, null)
}
