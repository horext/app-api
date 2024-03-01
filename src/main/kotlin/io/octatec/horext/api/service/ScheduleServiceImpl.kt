package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubjects
import io.octatec.horext.api.domain.Schedules
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScheduleServiceImpl() : ScheduleService {

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