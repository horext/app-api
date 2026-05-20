package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.SubjectRelationship
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

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
