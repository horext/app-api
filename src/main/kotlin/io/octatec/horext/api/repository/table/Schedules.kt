package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.Section
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

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
