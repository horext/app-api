package io.octatec.horext.api.domain

import java.time.Instant

data class Subject(
    val id: Long,
    var course: Course?,
    var studyPlan: StudyPlan?,
    var type: SubjectType?,
    var credits: Int?,
    var cycle: Int?,
    var relationships: List<SubjectRelationship> = ArrayList(),
    var createdAt: Instant? = null,
    var updatedAt: Instant? = null,
) {
    constructor(id: Long) : this(id, null, null, null, null, null)
}
