package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ScheduleSubject

interface ScheduleSubjectService {
    fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<ScheduleSubject>

    fun getAllByIds(ids: List<Long>): List<ScheduleSubject>
}
