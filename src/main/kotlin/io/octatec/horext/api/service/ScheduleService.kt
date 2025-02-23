package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Schedule

interface ScheduleService {
    fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<Schedule>
}
