package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

data class SubjectRelationship(
    val id: Long,
    val subjectId: Long,
    val relatedSubjectId: Long,
    val relationshipTypeId: Long?,
)

object SubjectRelationships : LongIdTable("subject_relationship") {
    val subjectId = long("to_subject_id")
    val relatedSubjectId = long("from_subject_id")
    val relationshipTypeId = long("subject_relationship_type_id").nullable()

    fun createEntity(row: ResultRow): SubjectRelationship =
        SubjectRelationship(
            id = row[id].value,
            subjectId = row[subjectId],
            relatedSubjectId = row[relatedSubjectId],
            relationshipTypeId = row[relationshipTypeId],
        )
}
