package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.Classroom
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Classrooms : LongIdTable("classroom") {
    val name = varchar("name", length = 100)

    val code = varchar("code", length = 50)

    fun createEntity(row: ResultRow): Classroom =
        Classroom(
            row[Classrooms.id].value,
            row[code],
            row[name],
        )
}
