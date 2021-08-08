package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.service.ScheduleService
import io.octatec.horext.api.service.ScheduleSubjectService
import io.octatec.horext.api.service.SubjectService
import io.octatec.horext.api.util.Pagination
import org.ktorm.dsl.map
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("schedules")
class ScheduleController(val scheduleService: ScheduleService) {

    @GetMapping(params = ["subject", "hourlyLoad"])
    fun getAllBySpeciality(
        @RequestParam(name = "subject") subjectId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long
    ): ResponseEntity<List<Schedule>> {
        return ResponseEntity<List<Schedule>>(
            scheduleService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId),
            HttpStatus.OK
        )
    }

}