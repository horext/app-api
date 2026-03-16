package io.octatec.horext.api.domain

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp
import java.time.Instant

data class Schedule(
    val id: Long,
    var section: Section?,
    var deleteAt: Instant?,
    var sessions: List<ClassSession>? = null,
) {
    constructor(id: Long) : this(id, null, null, null)
}

object Schedules : LongIdTable("schedule") {
    val deleteAt = timestamp("delete_at").nullable()

    val sectionId = reference("section_id", Sections)

    val vacancies = integer("vacancies").nullable()

    val updatedAt = timestamp("updated_at").nullable()

    val deletedAt = timestamp("deleted_at").nullable()

    fun createEntity(row: ResultRow): Schedule =
        Schedule(
            row[Schedules.id].value,
            Section(row[sectionId].value),
            row[deleteAt],
        )
}
