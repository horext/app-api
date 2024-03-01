package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ResultRow

/**
 * The organization unit entity.
 */
data class Course(

    val id: String,

    var name: String?
) {

    constructor(id: String) : this(id, null)
}

object Courses : IdTable<String>("course") {
    override val id = varchar(
        "id",
        length = 50
    ).entityId()

    override val primaryKey = PrimaryKey(id)

    val name = varchar("name", length = 100)

    fun createEntity(row: ResultRow): Course {
        return Course(
            row[id].value,
            row[name]
        )
    }

}
