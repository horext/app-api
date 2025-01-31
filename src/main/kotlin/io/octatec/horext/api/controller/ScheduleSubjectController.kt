package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.service.ScheduleSubjectService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("scheduleSubjects")
class ScheduleSubjectController(
    val scheduleSubjectService: ScheduleSubjectService,
) {
    @GetMapping(params = ["ids"])
    fun getAllByIds(
        @RequestParam(name = "ids") ids: List<Long>,
    ): List<ScheduleSubject> = scheduleSubjectService.getAllByIds(ids)

    @GetMapping(params = ["subject", "hourlyLoad"])
    fun getAllBySpeciality(
        @RequestParam(name = "subject") subjectId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long,
    ): List<ScheduleSubject> = scheduleSubjectService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)
}
