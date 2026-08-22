package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.repository.table.Courses
import io.octatec.horext.api.repository.table.ScheduleSubjects
import io.octatec.horext.api.repository.table.Schedules
import io.octatec.horext.api.repository.table.Subjects
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.anyFrom
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.select
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
            }.orderBy(
                ss.fromDate to SortOrder.ASC_NULLS_FIRST,
                ss.toDate to SortOrder.ASC_NULLS_LAST,
                ss.id to SortOrder.ASC,
            ).map { row -> ss.createEntity(row) }
    }

    override fun getAllByIds(ids: List<Long>): List<ScheduleSubject> {
        val ss = ScheduleSubjects
        val s = Subjects
        val c = Courses
        val skt = Schedules
        val scheduleSubjects =
            ss
                .innerJoin(s)
                .innerJoin(c)
                .innerJoin(skt)
                .select(ss.columns + s.columns + c.columns + skt.columns)
                .where(ss.id eq anyFrom(ids))
                .orderBy(ss.id to SortOrder.ASC)
                .map { row -> ss.createEntity(row) }

        val positionById = ids.withIndex().associate { (position, id) -> id to position }
        return scheduleSubjects.sortedBy { positionById[it.id] }
    }
}
