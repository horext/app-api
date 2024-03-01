package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.service.ScheduleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("schedules")
class ScheduleController(val scheduleService: ScheduleService) {

    @GetMapping(params = ["subject", "hourlyLoad"])
    fun getAllBySpeciality(
        @RequestParam(name = "subject") subjectId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long
    ): List<Schedule> {
        return scheduleService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)
    }

}