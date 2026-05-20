package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.Subject
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.timestamp

object ScheduleSubjects : LongIdTable("schedule_subject") {
    val fromDate = timestamp("from_datetime").nullable()

    val toDate = timestamp("to_datetime").nullable()

    val subjectId = reference("subject_id", Subjects)

    val hourlyLoadId = reference("hourly_load_id", HourlyLoads)

    val scheduleId = reference("schedule_id", Schedules)

    fun createEntity(row: ResultRow): ScheduleSubject =
        ScheduleSubject(
            id = row[id].value,
            fromDate = row[fromDate],
            toDate = row[toDate],
            subject =
                runCatching { Subjects.createEntity(row) }
                    .getOrElse { Subject(id = row[subjectId].value) },
            hourlyLoad =
                runCatching { HourlyLoads.createEntity(row) }
                    .getOrElse { HourlyLoad(id = row[hourlyLoadId].value) },
            schedule =
                runCatching { Schedules.createEntity(row) }
                    .getOrElse { Schedule(id = row[scheduleId].value) },
        )
}
