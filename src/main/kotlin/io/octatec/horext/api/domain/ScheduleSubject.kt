package io.octatec.horext.api.domain

import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.Instant

interface ScheduleSubject : Entity<ScheduleSubject> {
    companion object : Entity.Factory<ScheduleSubject>()

    val id: Long

    var fromDate: Instant

    var toDate: Instant

    var subject: Subject

    var hourlyLoad: HourlyLoad

    var schedule: Schedule
}

object ScheduleSubjects : Table<ScheduleSubject>("schedule_subject") {

    val id = long("id").primaryKey().bindTo { it.id }

    val fromDate = timestamp("from_datetime").bindTo { it.fromDate }

    val toDate = timestamp("to_datetime").bindTo { it.toDate }

    val subjectId = long("subject_id").references(Subjects) { it.subject }

    val hourlyLoadId = long("hourly_load_id").references(HourlyLoads) { it.hourlyLoad }

    val scheduleId = long("schedule_id").references(Schedules) { it.schedule }
}

val Database.scheduleSubjects get() = this.sequenceOf(ScheduleSubjects)