package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ClassSession


interface ClassSessionService {
    fun findByScheduleId(scheduleId: Long): List<ClassSession>

    fun findByScheduleIds(scheduleId: List<Long>): List<ClassSession>


}