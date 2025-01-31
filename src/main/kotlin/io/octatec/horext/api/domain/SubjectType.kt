package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

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
