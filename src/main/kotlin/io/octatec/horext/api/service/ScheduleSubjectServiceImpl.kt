package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.anyFrom
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class ScheduleSubjectServiceImpl() : ScheduleSubjectService {
    override fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<ScheduleSubject> {
        val ss = ScheduleSubjects
        val s = Schedules
        return ss
            .leftJoin(s)
            .select(ss.columns + s.columns)
            .where {
                (ss.subjectId eq subjectId) and
                        (ss.hourlyLoadId eq hourlyLoadId) and (s.deleteAt.isNull())
            }
            .map { row -> ss.createEntity(row) }

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