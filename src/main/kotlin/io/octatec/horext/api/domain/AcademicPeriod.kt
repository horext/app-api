package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

data class AcademicPeriod(
    val id: Long,
    var code: String?,
    var name: String?,
) {
    constructor(id: Long) : this(id, null, null)
}

object AcademicPeriods : LongIdTable("academic_period") {
    val code = varchar("code", length = 50).nullable()

    val name = varchar("name", length = 255).nullable()

    val fromDate = timestamp("from_date").nullable()

    val toDate = timestamp("to_date").nullable()
}
