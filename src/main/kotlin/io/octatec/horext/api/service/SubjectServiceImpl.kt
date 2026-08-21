package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.exception.ResourceNotFoundException
import io.octatec.horext.api.repository.SubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class SubjectServiceImpl(
    private val subjectRepository: SubjectRepository,
) : SubjectService {
    override fun getAllByStudyPlanId(studyPlanId: Long): List<Subject> = subjectRepository.getAllByStudyPlanId(studyPlanId)

    override fun getById(id: Long): Subject = subjectRepository.getById(id) ?: throw ResourceNotFoundException("Subject", "id", id)

    override fun getAllByIds(ids: List<Long>): List<Subject> = subjectRepository.getAllByIds(ids)

    override fun getPageBySearchAndFacultyIdAndHourlyLoad(
        search: String,
        facultyId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> = subjectRepository.getPageBySearchAndFacultyIdAndHourlyLoad(search, facultyId, hourlyLoadId, offset, limit)

    override fun getPageBySearchAndSpecialityIdAndHourlyLoad(
        search: String,
        specialityId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> = subjectRepository.getPageBySearchAndSpecialityIdAndHourlyLoad(search, specialityId, hourlyLoadId, offset, limit)

    override fun getPageBySearchAndStudyPlanIdAndHourlyLoad(
        search: String,
        studyPlanId: Long,
        hourlyLoadId: Long,
        offset: Int,
        limit: Int,
    ): Page<Subject> = subjectRepository.getPageBySearchAndStudyPlanIdAndHourlyLoad(search, studyPlanId, hourlyLoadId, offset, limit)

    override fun getAllByHourlyLoadIdAndStudyPlanIdAndCycle(
        hourlyLoadId: Long,
        studyPlanId: Long,
        cycle: Int,
    ): List<Subject> = subjectRepository.getAllByHourlyLoadIdAndStudyPlanIdAndCycle(hourlyLoadId, studyPlanId, cycle)
}
