package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SubjectServiceImpl(
    private val subjectRepository: SubjectRepository,
) : SubjectService {
    override fun getAllByStudyPlanId(studyPlanId: Long): List<Subject> = subjectRepository.getAllByStudyPlanId(studyPlanId)

    override fun getAllBySpecialityId(
        specialityId: Long,
        hourlyLoadId: Long,
    ): List<Subject> = subjectRepository.getAllBySpecialityId(specialityId, hourlyLoadId)

    override fun getAllBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
    ): List<Subject> = subjectRepository.getAllBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId)

    override fun getPageBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> = subjectRepository.getPageBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)

    override fun getAllBySpecialityIdAndHourlyLoadIdAndCycleId(
        specialityId: Long,
        hourlyLoadId: Long,
        cycleId: Int,
    ): List<Subject> = subjectRepository.getAllBySpecialityIdAndHourlyLoadIdAndCycleId(specialityId, hourlyLoadId, cycleId)
}
