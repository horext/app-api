package io.octatec.horext.api.domain

import io.octatec.horext.api.domain.ScheduleSubjects.references
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.toList
import org.ktorm.schema.*
import java.sql.Time
import java.time.Instant
import java.time.LocalTime
import javax.print.attribute.IntegerSyntax

interface ClassSession : Entity<ClassSession> {
    companion object : Entity.Factory<ClassSession>()

    val id: Long

    var schedule: Schedule

    var classSessionType: ClassSessionType

    var classroom: Classroom

    var teacher: Teacher

    var day: Int

    var startTime: LocalTime

    var endTime: LocalTime
}

open class ClassSessions(alias: String?)  : Table<ClassSession>("class_session", alias) {
    companion object : ClassSessions(null)
    override fun aliased(alias: String) = ClassSessions(alias)

    val id = long("id").primaryKey().bindTo { it.id }

    val scheduleId = long("schedule_id").references(Schedules) { it.schedule }

    val classroomId = long("classroom_id").references(Classrooms) { it.classroom }

    val teacherId = long("teacher_id").references(Teachers) { it.teacher }

    val classSessionTypeId = long("class_session_type_id").references(ClassSessionTypes) { it.classSessionType }

    val day = int("day").bindTo { it.day }

    val startTime = time("start_time").bindTo { it.startTime }

    val endTime = time("end_time").bindTo { it.endTime }


}


val Database.classSessions get() = this.sequenceOf(ClassSessions)