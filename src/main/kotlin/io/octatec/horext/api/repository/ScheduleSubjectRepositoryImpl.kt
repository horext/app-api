package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.Courses
import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.ScheduleSubjects
import io.octatec.horext.api.domain.Schedules
import io.octatec.horext.api.domain.Subjects
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.anyFrom
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository

@Repository
class ScheduleSubjectRepositoryImpl : ScheduleSubjectRepository {
    override fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<ScheduleSubject> {
        val ss = ScheduleSubjects
        val s = Schedules
        return ss
            .leftJoin(s)
            .select(ss.columns + s.columns)
            .where {
                (ss.subjectId eq subjectId) and
                    (ss.hourlyLoadId eq hourlyLoadId) and (s.deleteAt.isNull())
            }.map { row -> ss.createEntity(row) }
    }

    override fun getAllByIds(ids: List<Long>): List<ScheduleSubject> {
        val ss = ScheduleSubjects
        val s = Subjects
        val c = Courses
        val skt = Schedules
        return ss
            .innerJoin(s)
            .innerJoin(c)
            .innerJoin(skt)
            .select(ss.columns + s.columns + c.columns + skt.columns)
            .where(ss.id eq anyFrom(ids))
            .map { row -> ss.createEntity(row) }
    }
}
