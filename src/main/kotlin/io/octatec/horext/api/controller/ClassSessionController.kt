package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.ClassSession
import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.service.ClassSessionService
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
@RequestMapping("classSessions")
class ClassSessionController(val classSessionService: ClassSessionService) {

    @GetMapping(params = ["schedule"])
    fun getAllBySpeciality(
        @RequestParam(name = "schedule") scheduleId: Long,
    ): List<ClassSession> {
        return classSessionService.findByScheduleId(scheduleId)
    }

    @GetMapping(params = ["schedules"])
    fun getAllBySchedulesId(
        @RequestParam(name = "schedules") scheduleIds: List<Long>,
    ): List<ClassSession> {
        return classSessionService.findByScheduleIds(scheduleIds)
    }

}