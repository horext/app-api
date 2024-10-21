package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubjects
import io.octatec.horext.api.domain.Schedules
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class ScheduleRepositoryImpl : ScheduleRepository {

    override fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<Schedule> {
        val ss = ScheduleSubjects
        val s = Schedules
        return s
            .innerJoin(ss)
            .select(ss.columns)
            .where {
                (ss.subjectId eq subjectId) and
                        (ss.hourlyLoadId eq hourlyLoadId)
            }
            .map { row -> s.createEntity(row) }
    }
}
