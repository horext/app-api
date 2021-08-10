package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import org.ktorm.dsl.Query
import org.springframework.util.MultiValueMap


interface ScheduleService {
    fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<Schedule>

}