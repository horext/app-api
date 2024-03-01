package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow

data class Classroom(

    val id: Long,

    var code: String?,

    var name: String?,
) {

    constructor(id: Long) : this(id, null, null)
}

object Classrooms : LongIdTable("classroom") {


    val name = varchar("name", length = 100)

    val code = varchar("code", length = 50)

    fun createEntity(row: ResultRow): Classroom {
        return Classroom(
            row[Classrooms.id].value,
            row[code],
            row[name]
        )
    }
}

