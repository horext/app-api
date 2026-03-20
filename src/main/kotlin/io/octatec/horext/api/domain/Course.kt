package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.IdTable

/**
 * The organization unit entity.
 */
data class Course(
    val id: String,
    var name: String?,
) {
    constructor(id: String) : this(id, null)
}

object Courses : IdTable<String>("course") {
    override val id =
        varchar(
            "id",
            length = 50,
        ).entityId()

    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", length = 100).nullable()

    fun createEntity(row: ResultRow): Course =
        Course(
            row[id].value,
            row[name],
        )
}
