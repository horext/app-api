package io.octatec.horext.api.repository.table

import io.octatec.horext.api.domain.ClassSession
import io.octatec.horext.api.domain.ClassSessionType
import io.octatec.horext.api.domain.Classroom
import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.Teacher
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.javatime.time
import org.jetbrains.exposed.v1.javatime.timestamp

object ClassSessions : LongIdTable("class_session") {
    val scheduleId = reference("schedule_id", Schedules)

    val classroomId = reference("classroom_id", Classrooms)

    val teacherId = reference("teacher_id", Teachers)

    val classSessionTypeId = reference("class_session_type_id", ClassSessionTypes)

    val day = integer("day")

    val startTime = time("start_time")

    val endTime = time("end_time")

    val deletedAt = timestamp("deleted_at").nullable()

    val updatedAt = timestamp("updated_at").nullable()

    fun createEntity(row: ResultRow): ClassSession =
        ClassSession(
            row[id].value,
            runCatching { Schedules.createEntity(row) }.getOrElse {
                Schedule(
                    id = row[scheduleId].value,
                )
            },
            runCatching { ClassSessionTypes.createEntity(row) }.getOrElse {
                ClassSessionType(
                    id = row[classSessionTypeId].value,
                )
            },
            runCatching { Classrooms.createEntity(row) }.getOrElse {
                Classroom(
                    id = row[classroomId].value,
                )
            },
            runCatching { Teachers.createEntity(row) }.getOrElse {
                Teacher(
                    id = row[teacherId].value,
                )
            },
            row[day],
            row[startTime],
            row[endTime],
        )
}
