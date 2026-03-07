package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

data class Teacher(
    val id: Long,
    var code: String?,
    var fullName: String?,
) {
    constructor(id: Long) : this(id, null, null)
}

object Teachers : LongIdTable("teacher") {
    val fullName = varchar("full_name", length = 100)

    val code = varchar("code", length = 50)

    fun createEntity(row: ResultRow): Teacher =
        Teacher(
            row[id].value,
            row[code],
            row[fullName],
        )
}
