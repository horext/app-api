package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.Schedule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class ScheduleRepositoryTest {

    @Autowired
    private lateinit var scheduleRepository: ScheduleRepository

    @MockBean
    private lateinit var scheduleRepositoryMock: ScheduleRepository

    @Test
    fun testFindBySubjectIdAndHourlyLoadId() {
        val subjectId = 1L
        val hourlyLoadId = 1L
        val schedules = listOf(Schedule(1L), Schedule(2L))
        Mockito.`when`(scheduleRepositoryMock.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)).thenReturn(schedules)

        val result = scheduleRepository.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)

        assertEquals(schedules, result)
    }
}
