package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ClassSession

interface ClassSessionService {

    fun findByScheduleIds(scheduleIds: List<Long>): List<ClassSession>
}
