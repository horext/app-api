package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

data class SubjectType(
    val id: Long,
    var code: String?,
    var name: String?,
) {
    constructor(id: Long) : this(id, null, null)
}

object SubjectTypes : LongIdTable("subject_type") {
    val name = varchar("name", length = 100)

    val code = varchar("code", length = 50)

    fun createEntity(row: ResultRow): SubjectType =
        SubjectType(
            row[id].value,
            row[code],
            row[name],
        )
}
