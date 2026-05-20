package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.Course
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.IdTable

object Courses : IdTable<String>("course") {
    override val id =
        varchar(
            "id",
            length = 50,
        ).entityId()

    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", length = 300).nullable()

    fun createEntity(row: ResultRow): Course =
        Course(
            row[id].value,
            row[name],
        )
}
