package io.octatec.horext.api.domain

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.time
import java.time.LocalTime

data class ClassSession(
    val id: Long,

    var schedule: Schedule,

    var type: ClassSessionType,

    var classroom: Classroom,

    var teacher: Teacher,

    var day: Int,

    var startTime: LocalTime,

    var endTime: LocalTime
)

object ClassSessions : LongIdTable("class_session") {

    val scheduleId = reference("schedule_id", Schedules)

    val classroomId = reference("classroom_id", Classrooms)

    val teacherId = reference("teacher_id", Teachers)

    val classSessionTypeId = reference("class_session_type_id", ClassSessionTypes)

    val day = integer("day")

    val startTime = time("start_time")

    val endTime = time("end_time")


    fun createEntity(row: ResultRow): ClassSession {
        return ClassSession(
            row[id].value,
            runCatching { Schedules.createEntity(row) }.getOrElse {
                Schedule(
                    id = row[scheduleId].value
                )
            },
            runCatching { ClassSessionTypes.createEntity(row) }.getOrElse {
                ClassSessionType(
                    id = row[classSessionTypeId].value
                )
            },
            runCatching { Classrooms.createEntity(row) }.getOrElse {
                Classroom(
                    id = row[classroomId].value
                )
            },
            runCatching { Teachers.createEntity(row) }.getOrElse {
                Teacher(
                    id = row[teacherId].value
                )
            },
            row[day],
            row[startTime],
            row[endTime]
        )

    }
}
