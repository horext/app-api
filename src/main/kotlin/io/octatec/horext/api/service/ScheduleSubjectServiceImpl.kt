package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.repository.ScheduleSubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScheduleSubjectServiceImpl(
    private val scheduleSubjectRepository: ScheduleSubjectRepository,
) : ScheduleSubjectService {
    override fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<ScheduleSubject> = scheduleSubjectRepository.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)

    override fun getAllByIds(ids: List<Long>): List<ScheduleSubject> = scheduleSubjectRepository.getAllByIds(ids)
}
