package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page

interface SubjectService {
    fun getAllByStudyPlanId(studyPlanId: Long): List<Subject>

    fun getAllByIds(ids: List<Long>): List<Subject>

    fun getPageBySearchAndFacultyIdAndHourlyLoad(
        search: String,
        facultyId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject>

    fun getPageBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject>

    fun getPageBySearchAndStudyPlanIdAndHourlyLoad(
        search: String,
        studyPlanId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject>

    fun getAllByHourlyLoadIdAndStudyPlanIdAndCycle(
        hourlyLoadId: Long,
        studyPlanId: Long,
        cycle: Int,
    ): List<Subject>
}
