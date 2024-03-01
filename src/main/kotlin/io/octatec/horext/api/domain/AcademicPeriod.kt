package io.octatec.horext.api.domain


import org.jetbrains.exposed.dao.id.LongIdTable

data class AcademicPeriod(

    val id: Long,

    var code: String?,

    var name: String?

) {
    constructor(id: Long) : this(id, null, null)
}

object AcademicPeriods : LongIdTable("academic_period") {

    val code = varchar("code", length = 50)

    val name = varchar("name", length = 50)
}
