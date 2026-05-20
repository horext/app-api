package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.ClassSessionType
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object ClassSessionTypes : LongIdTable("class_session_type") {
    val name = varchar("name", length = 100)

    val code = varchar("code", length = 50)

    fun createEntity(row: ResultRow): ClassSessionType =
        ClassSessionType(
            row[ClassSessionTypes.id].value,
            row[code],
            row[name],
        )
}
