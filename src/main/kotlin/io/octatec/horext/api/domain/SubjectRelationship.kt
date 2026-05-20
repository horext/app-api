package io.octatec.horext.api.domain

data class SubjectRelationship(
    val id: Long,
    val subjectId: Long,
    val relatedSubjectId: Long,
    val relationshipTypeId: Long?,
)
