package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

data class ClassSessionType(
    val id: Long,

    var code: String?,

    var name: String?,
) {

    constructor(id: Long) : this(id, null, null)
}


object ClassSessionTypes : LongIdTable("class_session_type") {


    val name = varchar("name", length = 100)

    val code = varchar("code", length = 50)

    fun createEntity(row: ResultRow): ClassSessionType {
        return ClassSessionType(
            row[ClassSessionTypes.id].value,
            row[code],
            row[name]
        )
    }
}
