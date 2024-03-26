package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

data class ScheduleSubject(

    val id: Long,

    var fromDate: Instant?,

    var toDate: Instant?,

    var subject: Subject,

    var hourlyLoad: HourlyLoad,

    var schedule: Schedule
)

object ScheduleSubjects : LongIdTable("schedule_subject") {

    val fromDate = timestamp("from_datetime").nullable()

    val toDate = timestamp("to_datetime").nullable()

    val subjectId = reference("subject_id", Subjects)

    val hourlyLoadId = reference("hourly_load_id", HourlyLoads)

    val scheduleId = reference("schedule_id", Schedules)

    fun createEntity(row: ResultRow): ScheduleSubject {
        return ScheduleSubject(
            id = row[id].value,
            fromDate = row[fromDate],
            toDate = row[toDate],
            subject = runCatching { Subjects.createEntity(row) }
                .getOrElse { Subject(id = row[subjectId].value) },
            hourlyLoad = runCatching { HourlyLoads.createEntity(row) }
                .getOrElse { HourlyLoad(id = row[hourlyLoadId].value) },
            schedule = runCatching { Schedules.createEntity(row) }
                .getOrElse { Schedule(id = row[scheduleId].value) }
        )
    }
}
