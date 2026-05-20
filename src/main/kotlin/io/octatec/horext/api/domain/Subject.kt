package io.octatec.horext.api.domain

data class Subject(
    val id: Long,
    var course: Course?,
    var studyPlan: StudyPlan?,
    var type: SubjectType?,
    var credits: Int?,
    var cycle: Int?,
    var relationships: List<SubjectRelationship> = ArrayList(),
) {
    constructor(id: Long) : this(id, null, null, null, null, null)
}
