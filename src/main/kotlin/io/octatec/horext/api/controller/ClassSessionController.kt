package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.ClassSession
import io.octatec.horext.api.service.ClassSessionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("classSessions")
class ClassSessionController(
    val classSessionService: ClassSessionService,
) {
    @GetMapping(params = ["schedule"])
    fun getAllBySpeciality(
        @RequestParam(name = "schedule") scheduleId: Long,
    ): List<ClassSession> = classSessionService.findByScheduleId(scheduleId)

    @GetMapping(params = ["schedules"])
    fun getAllBySchedulesId(
        @RequestParam(name = "schedules") scheduleIds: List<Long>,
    ): List<ClassSession> = classSessionService.findByScheduleIds(scheduleIds)
}
