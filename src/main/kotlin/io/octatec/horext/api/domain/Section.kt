package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.IdTable

data class Section(
    val id: String,
)

object Sections : IdTable<String>("section") {
    override val id =
        varchar(
            "id",
            length = 50,
        ).entityId()

    override val primaryKey = PrimaryKey(id)
}
