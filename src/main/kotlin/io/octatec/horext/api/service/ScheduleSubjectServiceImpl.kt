package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.repository.ScheduleSubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScheduleSubjectServiceImpl(
    private val scheduleSubjectRepository: ScheduleSubjectRepository,
    private val classSessionService: ClassSessionService,
) : ScheduleSubjectService {
    override fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<ScheduleSubject> =
        scheduleSubjectRepository
            .findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)
            .initializeScheduleSessions()

    override fun getAllByIds(ids: List<Long>): List<ScheduleSubject> =
        scheduleSubjectRepository.getAllByIds(ids).initializeScheduleSessions()

    private fun List<ScheduleSubject>.initializeScheduleSessions(): List<ScheduleSubject> {
        val scheduleIds = map { it.schedule.id }.distinct()
        val sessionsByScheduleId =
            if (scheduleIds.isEmpty()) {
                emptyMap()
            } else {
                classSessionService
                    .findByScheduleIds(scheduleIds)
                    .groupBy { it.schedule.id }
            }

        forEach { scheduleSubject ->
            scheduleSubject.schedule.sessions = sessionsByScheduleId[scheduleSubject.schedule.id]
        }

        return this
    }
}
