package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
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

    @GetMapping(params = ["search","speciality","hourlyLoad"])
    fun getAllBySearch(
        @RequestParam(name = "search", required = true) search:String ,
        @RequestParam(name = "speciality") specialityId:Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId:Long,
        @RequestParam(name = "offset", defaultValue= "0") offset: Int,
        @RequestParam(name = "limit", defaultValue= "10") limit: Int
    ): ResponseEntity<Page<ScheduleSubject>> {
        Pagination.validatePageNumberAndSize(offset,limit)
        val page = scheduleSubjectService.getAllBySearchAndSpecialityIdAndHourlyLoad(
            search,specialityId,hourlyLoadId,offset,limit)
        return ResponseEntity<Page<ScheduleSubject>>(page , HttpStatus.OK)
    }

}