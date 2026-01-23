package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ScheduleSubject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class ScheduleSubjectRepositoryTest {

    @Autowired
    private lateinit var scheduleSubjectRepository: ScheduleSubjectRepository

    @MockBean
    private lateinit var scheduleSubjectRepositoryMock: ScheduleSubjectRepository

    @Test
    fun testFindBySubjectIdAndHourlyLoadId() {
        val subjectId = 1L
        val hourlyLoadId = 1L
        val scheduleSubjects = listOf(ScheduleSubject(), ScheduleSubject())
        Mockito.`when`(scheduleSubjectRepositoryMock.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)).thenReturn(scheduleSubjects)

        val result = scheduleSubjectRepository.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)

        assertEquals(scheduleSubjects, result)
    }

    @Test
    fun testGetAllByIds() {
        val ids = listOf(1L, 2L, 3L)
        val scheduleSubjects = listOf(ScheduleSubject(), ScheduleSubject())
        Mockito.`when`(scheduleSubjectRepositoryMock.getAllByIds(ids)).thenReturn(scheduleSubjects)

        val result = scheduleSubjectRepository.getAllByIds(ids)

        assertEquals(scheduleSubjects, result)
    }
}
