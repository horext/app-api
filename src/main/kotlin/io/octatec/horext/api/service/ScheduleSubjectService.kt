package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import org.ktorm.database.Database
import org.ktorm.dsl.Query
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.MultiValueMap


interface ScheduleSubjectService {


    fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<ScheduleSubject>

}