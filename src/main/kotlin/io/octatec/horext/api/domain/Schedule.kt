package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

data class Schedule(

    val id: Long,

    var section: Section?,

    var deleteAt: Instant?,
) {

    constructor(id: Long) : this(id, null, null)
}

object Schedules : LongIdTable("schedule") {

    val deleteAt = timestamp("delete_at")

    val sectionId = reference("section_id", Sections)

    fun createEntity(row: ResultRow): Schedule {
        return Schedule(
            row[Schedules.id].value,
            Section(row[sectionId].value),
            row[deleteAt]
        )
    }
}

