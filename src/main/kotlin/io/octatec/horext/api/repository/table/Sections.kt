package io.octatec.horext.api.repository.table

import org.jetbrains.exposed.v1.core.dao.id.IdTable

object Sections : IdTable<String>("section") {
    override val id =
        varchar(
            "id",
            length = 50,
        ).entityId()

    val code = varchar("code", length = 50).nullable()

    override val primaryKey = PrimaryKey(id)
}
