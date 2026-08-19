package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page

interface SubjectService {
    fun getAllByStudyPlanId(studyPlanId: Long): List<Subject>

    fun getAllBySpecialityId(
        specialityId: Long,
        hourlyLoadId: Long,
    ): List<Subject>

    fun getAllBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
    ): List<Subject>

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
