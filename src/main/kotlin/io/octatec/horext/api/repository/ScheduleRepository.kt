package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.Schedule

interface ScheduleRepository {
    fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<Schedule>
}
