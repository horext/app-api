package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ScheduleSubject

interface ScheduleSubjectRepository {
    fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<ScheduleSubject>
    fun getAllByIds(ids: List<Long>): List<ScheduleSubject>
}
