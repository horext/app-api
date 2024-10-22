package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.repository.ScheduleRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
class ScheduleServiceTest {

    @Autowired
    private lateinit var scheduleService: ScheduleService

    @MockBean
    private lateinit var scheduleRepository: ScheduleRepository

    @Test
    fun testFindBySubjectIdAndHourlyLoadId() {
        val subjectId = 1L
        val hourlyLoadId = 1L
        val schedules = listOf(Schedule(), Schedule())
        Mockito.`when`(scheduleRepository.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)).thenReturn(schedules)

        val result = scheduleService.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)

        assertEquals(schedules, result)
    }
}
