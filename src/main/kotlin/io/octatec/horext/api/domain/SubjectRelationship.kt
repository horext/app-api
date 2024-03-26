package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

data class SubjectRelationship(
    val id: Long,
    val subjectId: Long,
    val relatedSubjectId: Long,
    val relationshipTypeId: Long?
)

object SubjectRelationships: LongIdTable("subject_relationship") {
    val subjectId = long("to_subject_id")
    val relatedSubjectId = long("from_subject_id")
    val relationshipTypeId = long("subject_relationship_type_id").nullable()

    fun createEntity(row: ResultRow): SubjectRelationship {
        return SubjectRelationship(
            id = row[id].value,
            subjectId = row[subjectId],
            relatedSubjectId = row[relatedSubjectId],
            relationshipTypeId = row[relationshipTypeId]
        )
    }
}