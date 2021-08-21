package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.service.ScheduleSubjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("scheduleSubjects")
class ScheduleSubjectController(val scheduleSubjectService: ScheduleSubjectService) {


    @GetMapping(params = ["ids"])
    fun getAllByIds(
        @RequestParam(name = "ids") ids: List<Long>
    ): ResponseEntity<List<ScheduleSubject>> {
        return ResponseEntity<List<ScheduleSubject>>(
            scheduleSubjectService.getAllByIds(ids),
            HttpStatus.OK
        )
    }

    @GetMapping(params = ["subject", "hourlyLoad"])
    fun getAllBySpeciality(
        @RequestParam(name = "subject") subjectId: Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId: Long
    ): ResponseEntity<List<ScheduleSubject>> {
        return ResponseEntity<List<ScheduleSubject>>(
            scheduleSubjectService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId),
            HttpStatus.OK
        )
    }

}