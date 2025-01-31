package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ClassSession

interface ClassSessionRepository {
    fun findByScheduleId(scheduleId: Long): List<ClassSession>

    fun findByScheduleIds(scheduleIds: List<Long>): List<ClassSession>
}
