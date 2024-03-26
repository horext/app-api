package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

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
    fun createEntity(row: ResultRow): Teacher {
        return Teacher(
            row[id].value,
            row[code],
            row[fullName]
        )
    }
}
