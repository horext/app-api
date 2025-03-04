package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.repository.ScheduleSubjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class ScheduleSubjectServiceTest {

    @Autowired
    private lateinit var scheduleSubjectService: ScheduleSubjectService

    @MockBean
    private lateinit var scheduleSubjectRepository: ScheduleSubjectRepository

    @Test
    fun testFindBySubjectIdAndHourlyLoadId() {
        val subjectId = 1L
        val hourlyLoadId = 1L
        val scheduleSubjects = listOf(ScheduleSubject(), ScheduleSubject())
        Mockito.`when`(scheduleSubjectRepository.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)).thenReturn(scheduleSubjects)

        val result = scheduleSubjectService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)

        assertEquals(scheduleSubjects, result)
    }

    @Test
    fun testGetAllByIds() {
        val ids = listOf(1L, 2L, 3L)
        val scheduleSubjects = listOf(ScheduleSubject(), ScheduleSubject())
        Mockito.`when`(scheduleSubjectRepository.getAllByIds(ids)).thenReturn(scheduleSubjects)

        val result = scheduleSubjectService.getAllByIds(ids)

        assertEquals(scheduleSubjects, result)
    }
}
